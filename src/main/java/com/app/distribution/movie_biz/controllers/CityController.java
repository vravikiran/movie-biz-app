package com.app.distribution.movie_biz.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.distribution.movie_biz.entites.City;
import com.app.distribution.movie_biz.exceptions.DuplicateCityException;
import com.app.distribution.movie_biz.services.CityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(description = "Provides option to add/fetches cities where the app services are available", name = "creates/reads list of cities")
@RestController
@RequestMapping("/cities")
public class CityController {
	@Autowired
	CityService cityService;
	Logger logger = LoggerFactory.getLogger(CityController.class);

	@Operation(method = "POST", description = "Allows ADMIN/SUPER_ADMIN to add new cities where the app services are available")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Addition of new city successfull"),
			@ApiResponse(responseCode = "409", description = "City with given name already exists") })
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<Object> createCity(@RequestBody City city) throws DuplicateCityException {
		City createdCity = cityService.createCity(city);
		if (createdCity != null) {
			return ResponseEntity.ok(createdCity);
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("City with given name already exists");
		}
	}

	@Operation(method = "GET", description = "Provides the list of cities where app services are available")
	@ApiResponses(@ApiResponse(responseCode = "200", description = "List of cities are fetched successfully"))
	@GetMapping
	public ResponseEntity<List<City>> getListOfCities() {
		logger.info("Fetching the list of cities :: ");
		List<City> citiesList = cityService.getListOfCities();
		logger.info("Successfully fetched the list of cities");
		return ResponseEntity.ok(citiesList);
	}
}