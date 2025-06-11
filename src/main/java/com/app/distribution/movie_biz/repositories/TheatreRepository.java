package com.app.distribution.movie_biz.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.distribution.movie_biz.entites.Theatre;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Integer> {
	@Query(value = "select t from Theatre t where t.city_id= :city_id and t.isactive=true")
	public Page<Theatre> getTheatresByCity(@Param("city_id") int city_id, Pageable pageable);
	@Query(value ="select t from Theatre t where t.mobileno_hash=:mobileno_hash and t.isactive=true")
	public Page<Theatre> getTheatresByUser(@Param("mobileno_hash") String mobileno_hash, Pageable pageable);
	@Query(value="select user.mobileno from UserProfile user where user.mobileno_hash in ( select t.mobileno_hash from Theatre t where t.theatre_id=:theatre_id)")
	public long getUserbyTheatre(@Param("theatre_id") int theatre_id);
}