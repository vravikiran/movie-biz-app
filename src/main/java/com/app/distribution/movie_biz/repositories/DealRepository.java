package com.app.distribution.movie_biz.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.distribution.movie_biz.entites.Deal;
import com.app.distribution.movie_biz.entites.DealDetailInfo;
import com.app.distribution.movie_biz.entites.MovieDealCount;

@Repository
public interface DealRepository extends JpaRepository<Deal, Integer> {
	@Query(value = "select d as deal from Deal d where d.city_id = :city_id and d.showdate =:showdate and (:time IS NULL or d.showtime >= :time) and d.movieid = :movieid")
	public List<Deal> getDealsByMovieId(int movieid, LocalDate showdate, int city_id,@Param("time") LocalTime time);

	@Query(value="select count(*) as count, d.movieid as movieid from Deal d ,Movie m where d.city_id = :city_id and d.movieid = m.movieid and (:language IS NULL or m.language = :language) and (:name IS NULL or m.name like %:name% ) and (d.showdate > :current_date or (d.showdate = :current_date and d.showtime >= :current_time)) and m.isactive = true group by d.movieid order by m.release_date")
	public List<MovieDealCount> getMovieDealCountByCity(@Param("city_id") int city_id, @Param("name") String name,
			@Param("language") String language, @Param("current_date") LocalDate current_date,
			@Param("current_time") LocalTime current_time);

	@Query(value = "select distinct(d.showdate) from Deal d where d.movieid = :movieid and d.showdate >= :current_date order by showdate asc")
	public List<LocalDate> getDealDatesByMovie(@RequestParam int movieid,@Param("current_date") LocalDate current_date);

	@Query(value = "select m.name as moviename,m.movieid as movieid,m.release_date as moviereleasedate,t.theatre_id  as theatreid,t.theatre_name as theatrename,t.capacity as capacity, d.showdate as showdate,d.showtime as showtime,d.total_dealprice as totaldealprice from Deal  d, Movie m , Theatre t where d.dealid = :dealid and d.theatre_id = t.theatre_id and d.movieid = m.movieid")
	public DealDetailInfo getDealDetailedInfo(@Param("dealid") int dealid);

	@Query(value = "select d from Deal d, Theatre t  where d.showdate =:date and (:time IS NULL or d.showtime >= :time) and d.theatre_id = :theatre_id  and d.theatre_id =t.theatre_id")
	public List<Deal> getDealsByTheatreAndDate(@Param("theatre_id") int theatre_id, @Param("date") LocalDate date, @Param("time") LocalTime time);

	@Query(value = "select distinct(d.showdate) from Deal d WHERE d.is_invested = false and d.theatre_id = :theatre_id and d.showdate >= :current_date order by d.showdate ASC")
	public List<LocalDate> getDealDatesByTheatre(@Param("theatre_id") int theatre_id,@Param("current_date") LocalDate current_date);
	@Query(value = "select d.is_invested from Deal d where d.dealid = :dealid")
	public boolean isInvExistsForDeal(@Param("dealid") int dealid);
}