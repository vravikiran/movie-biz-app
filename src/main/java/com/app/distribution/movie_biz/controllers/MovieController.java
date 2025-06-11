package com.app.distribution.movie_biz.controllers;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.distribution.movie_biz.entites.Movie;
import com.app.distribution.movie_biz.exceptions.MovieNotFoundException;
import com.app.distribution.movie_biz.services.FileService;
import com.app.distribution.movie_biz.services.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Movie related API's", description = "Creates/updates movies")
@RestController
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	MovieService movieService;
	private static final String MOVIE_IMAGES_BUCKET = "tradeapp-movie";
	Logger logger = LoggerFactory.getLogger(MovieController.class);
	@Autowired
	FileService fileService;

	@Operation(method = "POST", description = "Creates a movie")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Movie created successfully"),
			@ApiResponse(responseCode = "403", description = "User is not authorized to create a movie"),
			@ApiResponse(responseCode = "400", description = "An DataIntegrityViolationException is thrown when a movie with given details already exists") })
	@PostMapping
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
		Movie createdMovie = movieService.createMovie(movie);
		return ResponseEntity.ok(createdMovie);
	}

	@Operation(method = "PATCH", description = "updates details of a movie with given movieid. Only SUPER_ADMIN and ADMIN can update a movie")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Movie details are updated successfully"),
			@ApiResponse(responseCode = "404", description = "Throws MovieNotFoundException if a movie not found with given movieid"),
			@ApiResponse(responseCode = "403", description = "User is not authorized to update details of a movie") })
	@PatchMapping("/update")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<Movie> updateMovie(@RequestParam int movieid, @RequestBody Map<String, Object> valuesToUpdate)
			throws MovieNotFoundException {
		Movie updatedMovie = movieService.updateMovie(movieid, valuesToUpdate);
		return ResponseEntity.ok(updatedMovie);
	}

	@Operation(method = "PATCH", description = "deactivates a movie with given movieid. Only SUPER_ADMIN and ADMIN can deactivate a movie")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Movie deactivated successfully"),
			@ApiResponse(responseCode = "404", description = "Throws MovieNotFoundException if a movie not found with given movieid"),
			@ApiResponse(responseCode = "403", description = "User is not authorized to deactivate a movie") })
	@PatchMapping("/deactivate")
	@PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
	public ResponseEntity<String> deactivateMovie(@RequestParam int movieid) throws MovieNotFoundException {
		movieService.deactivateMovie(movieid);
		return ResponseEntity.ok("Movie deactivated successfully");
	}

	@Operation(method = "POST", description = "Uploads image for a movie in AWS S3 bucket and returns the URL")
	@ApiResponse(responseCode = "200", description = "Uploads image for a movie in S3 bucket and returns the URL")
	@PostMapping(path = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadKycImage(@RequestPart("photo") MultipartFile file) throws IOException {
		logger.info("uploading movie image started");
		String url = fileService.uploadMovieImage(file, MOVIE_IMAGES_BUCKET);
		logger.info("movie image uploaded successfully");
		return ResponseEntity.ok(url);
	}

}