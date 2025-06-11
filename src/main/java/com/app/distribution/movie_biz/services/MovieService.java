package com.app.distribution.movie_biz.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.entites.Movie;
import com.app.distribution.movie_biz.exceptions.MovieNotFoundException;
import com.app.distribution.movie_biz.repositories.MovieRepository;
import com.app.distribution.movie_biz.util.Constants;

@Service
public class MovieService {
	@Autowired
	MovieRepository movieRepository;
	Logger logger = LoggerFactory.getLogger(MovieService.class);

	/**
	 * creates a new movie with given details
	 * 
	 * @param movie
	 * @return
	 * @throws DataIntegrityViolationException, if movie already exists with given
	 *                                          details
	 */
	public Movie createMovie(Movie movie) throws DataIntegrityViolationException {
		logger.info("Creation of movie started with details :: " + movie.toString());
		Movie createdMovie = null;
		try {
			createdMovie = movieRepository.save(movie);
			logger.info("Movie created successfully");
		} catch (DataIntegrityViolationException exception) {
			logger.error(
					"Creation of movie failed as combination of movie name/language/format already exists. Exceptin details :: "
							+ exception.getMessage());
			throw new DataIntegrityViolationException("combination of movie name/language/format already exists");
		}
		return createdMovie;
	}

	/**
	 * once the run of a movie completes, it is deactivated based on movieid
	 * 
	 * @param movieid
	 * @throws MovieNotFoundException if movieid is invalid
	 */
	public void deactivateMovie(int movieid) throws MovieNotFoundException {
		logger.info("Started deactivating movie with given id :: " + movieid);
		if (movieRepository.existsById(movieid)) {
			Movie movie = movieRepository.getReferenceById(movieid);
			movie.setIsactive(false);
			logger.info("succesfully deactivated movie :: " + movie.toString());
			movieRepository.save(movie);
		} else {
			logger.info("Movie with given id " + movieid + " doesn't exists");
			throw new MovieNotFoundException("Movie with given id doesn't exists");
		}
	}

	/**
	 * details of a movie are updated based on movieid
	 * 
	 * @param movieid
	 * @param valuesToUpdate
	 * @return
	 * @throws MovieNotFoundException
	 * @throws NoSuchElementException if invalid fields are provided in the provided
	 *                                json
	 */
	public Movie updateMovie(int movieid, Map<String, Object> valuesToUpdate)
			throws MovieNotFoundException, NoSuchElementException {
		logger.info("Updating movie details with id :: " + movieid);
		if (movieRepository.existsById(movieid)) {
			Movie movie = movieRepository.getReferenceById(movieid);
			try {
				if (valuesToUpdate.containsKey(Constants.RELEASE_DATE)) {
					movie.setRelease_date((LocalDate) valuesToUpdate.get(Constants.RELEASE_DATE));
					valuesToUpdate.remove(Constants.RELEASE_DATE);
				}
				if (valuesToUpdate.containsKey(Constants.DURATION)) {
					movie.setDuration(LocalTime.parse(valuesToUpdate.get(Constants.DURATION).toString()));
					valuesToUpdate.remove(Constants.DURATION);
				}
				if (valuesToUpdate.containsKey(Constants.GENRE)) {
					List<String> genres = movie.getGenre();
					@SuppressWarnings("unchecked")
					List<String> updatedGenres = (List<String>) valuesToUpdate.get("genre");
					genres.addAll(updatedGenres);
					movie.setGenre(genres);
					valuesToUpdate.remove("genre");
				}
				movie.updateValues(movie, valuesToUpdate);
			} catch (NoSuchElementException exception) {
				logger.info("one or more fields to be updated are not valid for a given movie with id :: " + movieid);
				throw new NoSuchElementException("One or more fields are not valid");
			}
			movieRepository.save(movie);
			logger.info("Movie updated succesfully with id :: " + movieid);
			return movie;
		} else {
			throw new MovieNotFoundException("Movie with given id doesn't exists");
		}
	}
}