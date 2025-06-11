package com.app.distribution.movie_biz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.distribution.movie_biz.entites.Theatre;
import com.app.distribution.movie_biz.exceptions.TheatreNotFoundException;
import com.app.distribution.movie_biz.exceptions.UserNotFoundException;
import com.app.distribution.movie_biz.repositories.TheatreRepository;
import com.app.distribution.movie_biz.repositories.UserProfileRepository;
import com.app.distribution.movie_biz.util.HashGenerator;

@Service
public class TheatreService {
	@Autowired
	TheatreRepository theatreRepository;
	@Autowired
	UserProfileRepository userProfileRepository;
	Logger logger = LoggerFactory.getLogger(TheatreService.class);

	/**
	 * creates a new theatre and assigns owner who initiated the creation
	 * 
	 * @param theatre
	 * @param mobileno
	 * @return
	 */
	public Theatre createTheatre(Theatre theatre, String mobileno) {
		logger.info("Started creating theatre with details :: " + theatre.toString());
		theatre.setMobileno_hash(HashGenerator.generateSHA256Hash(mobileno));
		Theatre createdTheatre = theatreRepository.save(theatre);
		logger.info("Theatre created successfully");
		return createdTheatre;
	}

	/**
	 * makes theatre operational for business based on theatre_id
	 * 
	 * @param theatre_id
	 * @throws TheatreNotFoundException
	 */
	public void EnableTheatreForBiz(@RequestParam int theatre_id) throws TheatreNotFoundException {
		Theatre theatre = getTheatreById(theatre_id);
		theatre.setIsactive(true);
		theatreRepository.save(theatre);
	}

	/**
	 * suspends/stops theatre operations
	 * 
	 * @param theatre_id
	 * @throws TheatreNotFoundException, if theatre not found with given theatre_id
	 */
	public void DeactivateTheatre(@RequestParam int theatre_id) throws TheatreNotFoundException {
		Theatre theatre = getTheatreById(theatre_id);
		theatre.setIsactive(false);
		theatreRepository.save(theatre);
	}

	/**
	 * returns the list of theatres available in a given city
	 * 
	 * @param city_id
	 * @param pageable
	 * @return
	 */
	public Page<Theatre> getTheatresbyCity(int city_id, Pageable pageable) {
		logger.info("Fetching theatres in a given city :: " + city_id);
		return theatreRepository.getTheatresByCity(city_id, pageable);
	}

	/**
	 * returns the list of theatres owned by a specific user
	 * @param mobileno
	 * @param pageable
	 * @return
	 * @throws UserNotFoundException, if user not found with given mobile number
	 */
	public Page<Theatre> getTheatresByUser(long mobileno, Pageable pageable) throws UserNotFoundException {
		if (userProfileRepository.existsById(HashGenerator.generateSHA256Hash(Long.toString(mobileno)))) {
			return theatreRepository.getTheatresByUser(HashGenerator.generateSHA256Hash(Long.toString(mobileno)),
					pageable);
		} else {
			throw new UserNotFoundException("User not found with given mobile number");
		}
	}

	/**
	 * updates theatre owner
	 * @param theatre_id
	 * @param mobileno
	 * @return
	 * @throws TheatreNotFoundException, if theatre not found with given theatre_id
	 * @throws UserNotFoundException, if user not found with given mobile number
	 */
	public Theatre updateTheatreOwner(@RequestParam int theatre_id, @RequestParam long mobileno)
			throws TheatreNotFoundException, UserNotFoundException {
		Theatre theatre = getTheatreById(theatre_id);
		if (userProfileRepository.existsById(HashGenerator.generateSHA256Hash(Long.toString(mobileno)))) {
			theatre.setMobileno_hash(HashGenerator.generateSHA256Hash(Long.toString(mobileno)));
		} else {
			throw new UserNotFoundException("User not found with given mobile number");
		}
		return theatreRepository.save(theatre);
	}

	/**
	 * returns theatre based on theatre_id
	 * @param theatre_id
	 * @return
	 * @throws TheatreNotFoundException
	 */
	private Theatre getTheatreById(int theatre_id) throws TheatreNotFoundException {
		if (theatreRepository.existsById(theatre_id)) {
			return theatreRepository.getReferenceById(theatre_id);
		} else {
			throw new TheatreNotFoundException("Theatre not found with given details");
		}
	}
}