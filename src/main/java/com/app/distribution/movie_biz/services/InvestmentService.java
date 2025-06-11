package com.app.distribution.movie_biz.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.entites.DealDetailInfo;
import com.app.distribution.movie_biz.entites.Investment;
import com.app.distribution.movie_biz.entites.InvestmentRequest;
import com.app.distribution.movie_biz.entites.Refund;
import com.app.distribution.movie_biz.entites.Transaction;
import com.app.distribution.movie_biz.enums.InvestmentStatusEnum;
import com.app.distribution.movie_biz.enums.RefundStatusEnum;
import com.app.distribution.movie_biz.enums.TransactionStatusEnum;
import com.app.distribution.movie_biz.exceptions.InvalidInvestmentStatusException;
import com.app.distribution.movie_biz.repositories.DealRepository;
import com.app.distribution.movie_biz.repositories.InvestmentRepository;
import com.app.distribution.movie_biz.repositories.TransactionRepository;

@Service
public class InvestmentService {
	@Autowired
	DealRepository dealRepository;
	@Autowired
	InvestmentRepository investmentRepository;
	@Autowired
	DealService dealService;
	Logger logger = LoggerFactory.getLogger(InvestmentService.class);
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	RefundService refundService;

	/**
	 * creates an investment based on investmentRequest information and returns created investment
	 * @param investmentRequest
	 * @return
	 */
	public Investment createInvestment(InvestmentRequest investmentRequest) {
		logger.info("Creation of investment started for deal with id  ::" + investmentRequest.getDealid());
		Investment investment = null;
		if (dealRepository.existsById(investmentRequest.getDealid())) {
			investment = new Investment();
			DealDetailInfo dealDetailInfo = dealRepository.getDealDetailedInfo(investmentRequest.getDealid());
			investment.setDealid(investmentRequest.getDealid());
			investment.setMovie_name(dealDetailInfo.getMoviename());
			investment.setMovie_release_date(dealDetailInfo.getMoviereleasedate());
			investment.setMovieid(dealDetailInfo.getMovieid());
			investment.setShowdate(dealDetailInfo.getShowdate());
			investment.setShowtime(dealDetailInfo.getShowtime());
			investment.setTheatre_id(dealDetailInfo.getTheatreid());
			investment.setTheatre_name(dealDetailInfo.getTheatrename());
			investment.setInvestedamt(dealDetailInfo.getTotaldealprice());
			investment.setStatus(InvestmentStatusEnum.PROCESSING.toString());
			investment.setInvestment_id(
					"MT" + String.valueOf(investmentRequest.getDealid()) + "-" + LocalDateTime.now().toString());
			investment.setHouse_capacity(dealDetailInfo.getCapacity());
			investment.setCreated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			investment.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			investment.setMobileno(investmentRequest.getMobileno());
			Transaction transaction = new Transaction();
			transaction.setTransaction_id(investmentRequest.getTransaction_id());
			transaction.setInvestment_id(investment.getInvestment_id());
			transaction.setTransaction_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			transaction.setAmount(investmentRequest.getAmounttopay());
			transaction.setStatus(TransactionStatusEnum.PENDING.name());
			transactionRepository.save(transaction);
			investmentRepository.save(investment);
			logger.info("Investment created successfully for deal with id :: " + investment.getDealid());
			return investment;
		} else {
			throw new NoSuchElementException("Deal with given ID doesn't exists :: " + investmentRequest.getDealid());
		}
	}

	/**
	 * returns investments based on investment status for a specific investor
	 * @param status
	 * @param mobileno
	 * @param pageable
	 * @return
	 * @throws InvalidInvestmentStatusException
	 */
	public Page<Investment> getInvestmentsbyStaus(String status, long mobileno, Pageable pageable)
			throws InvalidInvestmentStatusException {
		logger.info("Fetching investments based on status :: " + status);
		if (status != null && !InvestmentStatusEnum.invStatusValues().containsKey(status.toUpperCase())) {
			logger.info("Invalid investment status");
			throw new InvalidInvestmentStatusException("not a valid Investment Status");
		} else {
			List<String> statuses = null;
			if (status != null && status.equalsIgnoreCase(InvestmentStatusEnum.ONGOING.name())) {
				statuses = new ArrayList<>();
				statuses.add(InvestmentStatusEnum.ONGOING.name());
				statuses.add(InvestmentStatusEnum.PROCESSING.name());
			} else if (status != null && status.equalsIgnoreCase(InvestmentStatusEnum.COMPLETED.name())) {
				statuses = new ArrayList<>();
				statuses.add(status);
			}
			return investmentRepository.getInvestmentsByStatus(statuses, mobileno, pageable);
		}
	}
	
	/**
	 * verifies if any investment already exists for a given deal based on dealId
	 * @param dealId
	 * @return
	 */
	public boolean verifyInvestmentForDeal(int dealId) {
		boolean isInvExistsForDeal = dealRepository.isInvExistsForDeal(dealId);
		return isInvExistsForDeal && investmentRepository.isInvExistsForDeal(dealId);
	}

	/**
	 * When a movie release date is postponed/updated the existing investments are cancelled and refunds are initiated 
	 * to investors
	 * Based on movieid the existing investments are fetched
	 * @param movieid
	 */
	@Async
	public void cancelInvOnMovieReleaseDateUpdated(int movieid) {
		List<Investment> investments = investmentRepository.getInvestmentsByMovie(movieid);
		investments.forEach(inv->{
			inv.setStatus(InvestmentStatusEnum.CANCELLED.name());
			inv.setUpdated_date(LocalDate.now());
		});
		investmentRepository.saveAll(investments);
		investments.forEach(inv->{
			refundPayment(inv.getMobileno(),inv.getInvestedamt(),inv.getInvestment_id());
		});
	}
	
	/**
	 * Initiates refund for the cancelled investment
	 * @param mobileno
	 * @param refundAmount
	 * @param investmentId
	 */
	private void refundPayment(long mobileno, double refundAmount, String investmentId) {
		Refund refund = new Refund();
		refund.setRefund_amount(refundAmount);
		refund.setInvestment_id(investmentId);
		refund.setMobileno(mobileno);
		refund.setStatus(RefundStatusEnum.INITIATED.name());
		refundService.initiateRefund(refund);
	}
	
}