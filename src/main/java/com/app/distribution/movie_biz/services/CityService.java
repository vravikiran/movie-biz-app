package com.app.distribution.movie_biz.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.entites.City;
import com.app.distribution.movie_biz.exceptions.DuplicateCityException;
import com.app.distribution.movie_biz.repositories.CityRepository;

@Service
public class CityService {
	@Autowired
	CityRepository cityRepository;
	Logger logger = LoggerFactory.getLogger(CityService.class);

	/**
	 * returns the list of cities where app services are available
	 * @return
	 */
	public List<City> getListOfCities() {
		logger.info("Fetching list of cities");
		return cityRepository.findAll();
	}
	
	/**
	 * Add a new city where app services will be available. Verifies if the city is already present
	 * @param city
	 * @return
	 * @throws DuplicateCityException 
	 */
	public City createCity(City city) throws DuplicateCityException {
		if(!cityRepository.verifyCityByName(city.getCity_name()))
		return cityRepository.save(city);
		else {
			throw new DuplicateCityException("City with given details already exists");
		}
			
	}
}