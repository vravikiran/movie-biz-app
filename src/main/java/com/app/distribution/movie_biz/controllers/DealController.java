package com.app.distribution.movie_biz.controllers;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.distribution.movie_biz.entites.Deal;
import com.app.distribution.movie_biz.entites.DealCountRequest;
import com.app.distribution.movie_biz.entites.DealIdAndDealPrice;
import com.app.distribution.movie_biz.entites.DealRequestObj;
import com.app.distribution.movie_biz.entites.DealResult;
import com.app.distribution.movie_biz.entites.MovieDealCountPojo;
import com.app.distribution.movie_biz.entites.TheatreDeal;
import com.app.distribution.movie_biz.entites.TheatreDealRequest;
import com.app.distribution.movie_biz.services.DealService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Deal - Details of a deal for specific movie in a specific theatre on a given date and show time", description = "Allows ADMIN/SUPER_ADMIN to create deals, update deal prices,"
		+ "investors can search deals based on few parameters like movie,city,theatre and specific dates")
@RestController
@RequestMapping("/deal")
public class DealController {
	@Autowired
	DealService dealService;
	Logger logger = LoggerFactory.getLogger(DealController.class);

	@Operation(method = "POST", description = "Allows ADMIN/SUPER_ADMIN to create new deals for a specific theatre and movie")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "deal creation successfull"),
			@ApiResponse(responseCode = "403", description = "Unauthorized to create a deal") })
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
		logger.info("Creation of deal started with details  :: " + deal.toString());
		Deal createdDeal = dealService.createDeal(deal);
		logger.info("Deal created successfully");
		return ResponseEntity.ok(createdDeal);
	}

	@Operation(method = "GET", description = "Fetches deals based on movie, city. If date is provided, returns deals for "
			+ "that specific date. If date is not provided,fetches deals available from current date and time")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fetches the matched deals with provided search parameter values") })
	@GetMapping("/movie")
	public ResponseEntity<Page<DealResult>> getDealsByMovieAndDate(@RequestParam int movieid,
			@RequestParam(required = false) LocalDate date, @RequestParam int city_id,
			@RequestParam(defaultValue = "0") int page, @RequestParam int size) {
		logger.info("retrieving deals by movie id and date :: ");
		Pageable pageable = PageRequest.of(page, size);
		DealRequestObj dealRequestObj = new DealRequestObj(movieid, date, city_id);
		Page<DealResult> deals = dealService.getDealsByMovie(dealRequestObj, pageable);
		logger.info("completed fetching deals by movie and date");
		return ResponseEntity.ok(deals);
	}

	@Operation(method = "GET", description = "Fetches count of deals per movie based on few search parameters like city,movie language,name of movie from current date and time")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fetches the count of deals per movie with provided search parameter values") })
	@GetMapping("/search")
	public ResponseEntity<Page<MovieDealCountPojo>> getMovieDealCountByCity(@RequestParam int city_id,
			@RequestParam(required = false) String name, @RequestParam(required = false) String language,
			@RequestParam(defaultValue = "0") int page, @RequestParam int size) {
		DealCountRequest dealCountRequest = new DealCountRequest(language, city_id, name);
		logger.info("retrieving count of deals using date and city ");
		Pageable pageable = PageRequest.of(page, size);
		Page<MovieDealCountPojo> result = dealService.getMovieDealCountByCity(dealCountRequest, pageable);
		logger.info("completed fetching count of deals by date and city");
		return ResponseEntity.ok(result);
	}

	@Operation(method = "GET", description = "Fetches deals available for a theatre on a given date. If date is not provided fetches deals from current date and time")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Fetches deals available for a theate on a gicen date") })
	@GetMapping("/theatre")
	public ResponseEntity<Page<TheatreDeal>> getDealsByTheatreAndDate(@RequestParam int theatre_id,
			@RequestParam(required = false) LocalDate date, @RequestParam(defaultValue = "0") int page,
			@RequestParam int size) {
		logger.info("Fetching deals in a theatre and given date ::" + theatre_id + "," + date);
		Pageable pageable = PageRequest.of(page, size);
		TheatreDealRequest dealRequest = new TheatreDealRequest(theatre_id, date);
		Page<TheatreDeal> result = dealService.getDealsByTheatre(dealRequest, pageable);
		logger.info("Fetched list of deals in a given theatre and given date");
		return ResponseEntity.ok(result);
	}

	@Operation(method = "GET", description = "Returns the list of dates where deals are available for a specific movie")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Returns the list of dates where deals are available for a specific movie") })
	@GetMapping("/dates/movie")
	public ResponseEntity<List<LocalDate>> getDealDatesByMovie(@RequestParam int movieid) {
		logger.info("Getting dates of deals for given movie with id :: " + movieid);
		List<LocalDate> dates = dealService.getDealDatesByMovie(movieid);
		logger.info("Successfully fetched deal dates for a given movie :: " + movieid);
		return ResponseEntity.ok(dates);
	}

	@Operation(method = "GET", description = "Returns the list of datas where deals are available in a specific theatre")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Returns the list of datas where deals are available in a specific theatre") })
	@GetMapping("/dates/theatre")
	public ResponseEntity<List<LocalDate>> getDealDatesByTheatre(@RequestParam int theatre_id) {
		logger.info("Fetching dates of deals for given theatre with id :: " + theatre_id);
		List<LocalDate> dates = dealService.getDealDatesByTheatre(theatre_id);
		logger.info("Successfully fetched deal dates for a given theatre with id :: " + theatre_id);
		return ResponseEntity.ok(dates);
	}

	@Operation(method = "POST", description = "Allows ADMIN/SUPER_ADMIN to update the price of a deal")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "deal price updated successfully") })
	@PostMapping("/update/dealprice")
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<String> updateDealPrice(@RequestBody DealIdAndDealPrice dealPrice) {
		logger.info("Updating deal price for a deal with id ::" + dealPrice.getDealid());
		dealService.updateDealPrice(dealPrice.getDealid(), dealPrice.getTotal_dealprice());
		return ResponseEntity.ok("deal price updated successfully");
	}
}