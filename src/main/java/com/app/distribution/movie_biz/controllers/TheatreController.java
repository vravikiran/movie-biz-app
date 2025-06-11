package com.app.distribution.movie_biz.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.distribution.movie_biz.entites.Theatre;
import com.app.distribution.movie_biz.exceptions.TheatreNotFoundException;
import com.app.distribution.movie_biz.exceptions.UserNotFoundException;
import com.app.distribution.movie_biz.services.TheatreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Theatre Management", description = "Creates a new theatre, activate/deactivate theatre")
@RestController
@RequestMapping("/theatre")
public class TheatreController {
	@Autowired
	TheatreService theatreService;
	Logger logger = LoggerFactory.getLogger(TheatreController.class);

	@Operation(method = "POST", description = "Creates a Theatre. Only ADMIN and SUPER_ADMIN users are allowed to create a theatre")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Creates a Theatre successfully"),
			@ApiResponse(responseCode = "403", description = "User is not authorized to create a Theatre") })
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<Theatre> createTheatre(@RequestBody Theatre theatre, Authentication authentication) {
		logger.info("Started creating theatre with given details :: " + theatre.toString());
		Theatre createdTheatre = theatreService.createTheatre(theatre, authentication.getName());
		logger.info("Theatre created successfully");
		return ResponseEntity.ok(createdTheatre);
	}

	@Operation(method = "PATCH", description = "Makes a Theatre operational for business.Only ADMIN and SUPER_ADMIN users are allowed to perform this operation")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Updates Theatre operational for business"),
			@ApiResponse(responseCode = "403", description = "User is not authorized to make a Theatre operational") })
	@PatchMapping("/enable")
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<String> EnableTheatreForBiz(@RequestParam int theatre_id) throws TheatreNotFoundException {
		theatreService.EnableTheatreForBiz(theatre_id);
		return ResponseEntity.ok("Theatre operational for business");
	}

	@Operation(method = "PATCH", description = "Theatre operation suspended/stopped for business.Only ADMIN and SUPER_ADMIN users are allowed to perform this operation")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Theatre operation suspended/stopped"),
			@ApiResponse(responseCode = "403", description = "User is not authorized to suspend/stopped a Theatre operations") })
	@PatchMapping("/deactivate")
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<String> DeactivateTheatre(@RequestParam int theatre_id) throws TheatreNotFoundException {
		theatreService.DeactivateTheatre(theatre_id);
		return ResponseEntity.ok("Theatre operation suspended/stopped");
	}

	@Operation(method = "GET", description = "returns the list of theatres in a city")
	@ApiResponse(responseCode = "200", description = "Successfully fetched theatres in a city")
	@GetMapping
	public ResponseEntity<Page<Theatre>> getTheatresByCity(@RequestParam int city_id,
			@RequestParam(defaultValue = "0") int page, @RequestParam int size) {
		logger.info("Fetching theatres in given city :: " + city_id);
		Pageable pageable = PageRequest.of(page, size);
		Page<Theatre> theatres = theatreService.getTheatresbyCity(city_id, pageable);
		logger.info("Successfully fetched theatres in city with id :: " + city_id);
		return ResponseEntity.ok().body(theatres);
	}

	@Operation(method = "GET", description = "Returns the list of theatres owned by a specific user")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Returns the list of theatres owned by a specific user"),
			@ApiResponse(responseCode = "404", description = "User not found with given mobile number") })
	@GetMapping("/user")
	public ResponseEntity<Page<Theatre>> getTheatresByUser(@RequestParam long mobileno,
			@RequestParam(defaultValue = "0") int page, @RequestParam int size) throws UserNotFoundException {
		logger.info("Fetching theatres associated with user :: " + mobileno);
		Pageable pageable = PageRequest.of(page, size);
		Page<Theatre> theatres = theatreService.getTheatresByUser(mobileno, pageable);
		return ResponseEntity.ok().body(theatres);
	}

	@Operation(method = "PUT", description = "Updates/changes the owner of theatre")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Updates/changes the owner of theatre"),
			@ApiResponse(responseCode = "404", description = "User not found with given mobile number, Theatre doesn't exists with given theatre id") })
	@PutMapping("/update-owner")
	public ResponseEntity<Theatre> updateTheatreOwner(@RequestParam int theatre_id, @RequestParam long mobileno)
			throws TheatreNotFoundException, UserNotFoundException {
		Theatre theatre = theatreService.updateTheatreOwner(theatre_id, mobileno);
		return ResponseEntity.ok(theatre);
	}
}