package com.app.distribution.movie_biz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.distribution.movie_biz.entites.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
	@Query("select case when count(c) > 0 then true else false end from City c where upper(c.city_name)= upper(:city)")
	public boolean verifyCityByName(String city);
}