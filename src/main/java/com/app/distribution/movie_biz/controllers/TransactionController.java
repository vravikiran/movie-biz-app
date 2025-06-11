package com.app.distribution.movie_biz.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.distribution.movie_biz.entites.PaymentResponse;
import com.app.distribution.movie_biz.services.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "Save Payment details", description = "Saves the payment response received from PhonePe once payment is completed for an investment. Updates the deal investment status and investment status")
@RestController
@RequestMapping("/transaction")
public class TransactionController {
	Logger logger = LoggerFactory.getLogger(TransactionController.class);
	@Autowired
	TransactionService transactionService;

	@Operation(method = "POST", description = "Processes the payment response received from PhonePe and updates investment and payment details")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Transaction details updated successfully"),
			@ApiResponse(responseCode = "400", description = "X-VERIFY header missing/unverified transaction") })
	@PostMapping("/details/update")
	public ResponseEntity<String> saveTransactionDetails(HttpServletRequest request, @RequestBody String response)
			throws JsonMappingException, JsonProcessingException {
		logger.info("Response received from phonepe");
		Gson gson = new Gson();
		PaymentResponse paymentResponse = gson.fromJson(response, PaymentResponse.class);
		logger.info("updating transaction details started");
		boolean isVerified = false;
		String xVerifyHeader = request.getHeader("X-VERIFY");
		logger.info("xVerifyHeader ::" + xVerifyHeader);
		if (xVerifyHeader != null) {
			isVerified = transactionService.verifyXVerifyHeader(xVerifyHeader, response);
		}
		if (!isVerified) {
			logger.info("X-VERIFY header missing/unverified transaction");
			return ResponseEntity.badRequest().body("X-VERIFY header missing/unverified transaction");
		} else {
			logger.info("Saving the transaction details");
			transactionService.savePaymentDetails(paymentResponse);
		}
		logger.info("updating transaction details completed");
		return ResponseEntity.ok().build();
	}

}