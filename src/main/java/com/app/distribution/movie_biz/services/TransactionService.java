package com.app.distribution.movie_biz.services;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.config.PhonepeConfig;
import com.app.distribution.movie_biz.entites.Investment;
import com.app.distribution.movie_biz.entites.PaymentInformation;
import com.app.distribution.movie_biz.entites.PaymentResponse;
import com.app.distribution.movie_biz.entites.Transaction;
import com.app.distribution.movie_biz.entites.TransactionDetails;
import com.app.distribution.movie_biz.enums.InvestmentStatusEnum;
import com.app.distribution.movie_biz.enums.TransactionStatusEnum;
import com.app.distribution.movie_biz.repositories.InvestmentRepository;
import com.app.distribution.movie_biz.repositories.TransactionDetailsRepository;
import com.app.distribution.movie_biz.repositories.TransactionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.sdk.pg.Env;
import com.phonepe.sdk.pg.payments.v1.PhonePePaymentClient;

@Service
public class TransactionService {
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	InvestmentRepository investmentRepository;
	@Autowired
	InvestmentService investmentService;
	@Autowired
	DealService dealService;
	@Autowired
	MessageService messageService;
	@Autowired
	TransactionDetailsRepository transactionDetailsRepository;

	@Autowired
	PhonepeConfig phonepeConfig;
	Logger logger = LoggerFactory.getLogger(TransactionService.class);

	/**
	 * Once the payment is completed through phonepe payment app a response is
	 * generated. This is a callback api to save the transaction details and update
	 * deal and investment status as per the response from phonpe payment app
	 * 
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public void savePaymentDetails(PaymentResponse pgResponse) throws JsonMappingException, JsonProcessingException {
		logger.info("Updating the transaction details received from phonepe");
		String res = new String(Base64.getDecoder().decode(pgResponse.getResponse()));
		ObjectMapper objectMapper = new ObjectMapper();
		PaymentInformation paymentInformation = objectMapper.readValue(res, PaymentInformation.class);
		String transactionId = paymentInformation.getData().getMerchantTransactionId();
		Transaction transaction = transactionRepository.getReferenceById(transactionId);
		transaction.setStatus(paymentInformation.getData().getState());
		transactionRepository.save(transaction);
		updateInvAndDealInfo(transactionId);
		logger.info("updated transaction status for the merchant transaction id :: " + transactionId);
		TransactionDetails tranDetails = convertPaymentInfoToTransDetails(paymentInformation);
		transactionDetailsRepository.save(tranDetails);
		logger.info("saved the transaction details");

	}

	/**
	 * Retrieves the transaction details from the payment information received from
	 * PhonePe
	 * 
	 * @param paymentInformation
	 * @return TransactionDetails
	 */
	private TransactionDetails convertPaymentInfoToTransDetails(PaymentInformation paymentInformation) {
		TransactionDetails tranDetails = new TransactionDetails();
		if (paymentInformation.getData() != null) {
			tranDetails.setMerchant_id(paymentInformation.getData().getMerchantId());
			tranDetails.setMerchant_transaction_id(paymentInformation.getData().getMerchantTransactionId());
			tranDetails.setTransaction_id(paymentInformation.getData().getTransactionId());
			tranDetails.setAmount(paymentInformation.getData().getAmount() / 100);
			tranDetails.setState(paymentInformation.getData().getState());
			tranDetails.setResponse_code(paymentInformation.getData().getResponseCode());
			if (paymentInformation.getData().getPaymentInstrument() != null) {
				tranDetails.setPayment_type(paymentInformation.getData().getPaymentInstrument().getType());
				tranDetails.setUtr(paymentInformation.getData().getPaymentInstrument().getUtr());
				tranDetails.setIfsc(paymentInformation.getData().getPaymentInstrument().getIfsc());
				tranDetails.setUpi_transaction_id(
						paymentInformation.getData().getPaymentInstrument().getUpiTransactionId());
				tranDetails.setCard_network(paymentInformation.getData().getPaymentInstrument().getCardNetwork());
				tranDetails.setAccount_type(paymentInformation.getData().getPaymentInstrument().getAccountType());
				tranDetails
						.setPg_transaction_id(paymentInformation.getData().getPaymentInstrument().getPgTransactionId());
				tranDetails.setPg_service_transaction_id(
						paymentInformation.getData().getPaymentInstrument().getPgServiceTransactionId());
				tranDetails.setBank_transaction_id(
						paymentInformation.getData().getPaymentInstrument().getBankTransactionId());
				tranDetails.setBank_id(paymentInformation.getData().getPaymentInstrument().getBankId());
				tranDetails.setArn(paymentInformation.getData().getPaymentInstrument().getArn());
				tranDetails.setCard_type(paymentInformation.getData().getPaymentInstrument().getCardType());
				tranDetails.setPg_authorization_code(
						paymentInformation.getData().getPaymentInstrument().getPgAuthorizationCode());
				tranDetails.setUnmasked_account_number(
						paymentInformation.getData().getPaymentInstrument().getUnmaskedAccountNumber());
				tranDetails.setBrn(paymentInformation.getData().getPaymentInstrument().getBrn());
			}
		}
		return tranDetails;
	}

	/**
	 * Updates the deal investment status to true and updates the Investment status
	 * based on the payment information received from PhonePe
	 * 
	 * @param transactionId
	 */
	public void updateInvAndDealInfo(String transactionId) {
		logger.info("updating investment and deal status for given transaction id :: " + transactionId);
		Transaction transaction = transactionRepository.getReferenceById(transactionId);
		String investment_id = transaction.getInvestment_id();
		Investment investment = investmentRepository.getReferenceById(investment_id);
		if (TransactionStatusEnum.valueOf(transaction.getStatus().toUpperCase()) != null) {
			if (transaction.getStatus().toUpperCase().equals(TransactionStatusEnum.COMPLETED.name())) {
				investment.setStatus(InvestmentStatusEnum.ONGOING.name());
				investmentRepository.save(investment);
				logger.info("Investment status updated");
				dealService.updateDealStatus(true, investment.getDealid());
				messageService.sendTransMsgToSeller(investment.getDealid());
				logger.info("deal status updated");
			} else if (transaction.getStatus().toUpperCase().equals(TransactionStatusEnum.FAILED.name())
					|| transaction.getStatus().toUpperCase().equals(TransactionStatusEnum.PENDING.name())) {
				investment.setStatus(InvestmentStatusEnum.CANCELLED.name());
				investmentRepository.save(investment);
				logger.info("Investment status updated");
				dealService.updateDealStatus(false, investment.getDealid());
				logger.info("deal status updated");
			}
		}
	}

	/**
	 * verifies whether the xVerify header received from PhonePe Payment response is
	 * valid
	 * 
	 * @param xVerify
	 * @param responseBody
	 * @return
	 */
	public boolean verifyXVerifyHeader(String xVerify, String responseBody) {
		logger.info("Verification of transaction details started");
		logger.info("xVerify ::" + xVerify);
		PhonePePaymentClient phonePeClient = new PhonePePaymentClient(phonepeConfig.getPhonePeMerchantId(),
				phonepeConfig.getPhonePeSaltKey(), Integer.valueOf(phonepeConfig.getPhonePeSaltIndex()), Env.PROD,
				true);
		logger.info("verification of transaction details completed");
		return phonePeClient.verifyResponse(xVerify, responseBody);
	}

}