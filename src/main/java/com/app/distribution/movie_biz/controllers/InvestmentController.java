package com.app.distribution.movie_biz.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.distribution.movie_biz.entites.Investment;
import com.app.distribution.movie_biz.entites.InvestmentRequest;
import com.app.distribution.movie_biz.exceptions.InvalidInvestmentStatusException;
import com.app.distribution.movie_biz.services.InvestmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Creates investments", description = "Investments are created for a given deal, throws exception if an investment already exists")
@RestController
@RequestMapping("/investment")
public class InvestmentController {

	@Autowired
	InvestmentService investmentService;
	Logger logger = LoggerFactory.getLogger(InvestmentController.class);

	@Operation(method = "POST", description = "Investor initiates a transaction for a specific deal")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Investment created successfully with PROCESSING status for a specific deal and investor") })
	@PostMapping
	public ResponseEntity<Investment> createOrder(@RequestBody InvestmentRequest investmentRequest)
			throws NoSuchElementException {
		logger.info("started creating investment for deal with id :: " + investmentRequest.getDealid());
		Investment investment = investmentService.createInvestment(investmentRequest);
		logger.info("Investment created successfully for deal with id :: " + investmentRequest.getDealid());
		return ResponseEntity.ok().body(investment);
	}

	@Operation(method = "GET", description = "Returns investment with all statuses are returned if status value is null,"
			+ "If the status provided is invalid throws an exception " + "returns investment with specific status")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Returns investments with given status"),
			@ApiResponse(responseCode = "400", description = "throws InvalidInvestmentStatusException if given status is invalid") })
	@GetMapping
	public ResponseEntity<Page<Investment>> getInvestmentsByStatus(@RequestParam(required = false) String status,
			@RequestParam long mobileno, @RequestParam(defaultValue = "0") int page, @RequestParam int size)
			throws InvalidInvestmentStatusException {
		logger.info("Fetching investments with status :: " + status);
		Pageable pageable = PageRequest.of(page, size);
		Page<Investment> investments = investmentService.getInvestmentsbyStaus(status, mobileno, pageable);
		logger.info("Successfully fetched investments with status :: " + status);
		return ResponseEntity.ok().body(investments);
	}

	@Operation(method = "GET", description = "Verifies if an investment already exists for given deal")
	@ApiResponses({ @ApiResponse(responseCode = "212", description = "No investments exists for given deal"),
			@ApiResponse(responseCode = "211", description = "Investmes exists/is in progress for given deal") })
	@GetMapping("/verify")
	public ResponseEntity<String> verifyInvForDeal(@RequestParam int dealId) {
		logger.info("Verifying if any investment exists for given dealId :: " + dealId);
		boolean isExists = investmentService.verifyInvestmentForDeal(dealId);
		if (isExists) {
			logger.info("investment exists/is in progress for given dealId :: " + dealId);
			return ResponseEntity.status(211)
					.body("Deal is already booked/investment is in progress for given deal :: " + dealId);
		} else {
			logger.info("No investment exists for given dealId :: " + dealId);
			return ResponseEntity.status(212).body("No investment exists for given deal id");
		}
	}
}