package com.app.distribution.movie_biz.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.distribution.movie_biz.entites.Investment;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, String> {
	@Query(value = "select inv from Investment inv where inv.mobileno = :mobileno and (:statuses IS NULL or inv.status in :statuses) order by inv.created_date desc")
	public Page<Investment> getInvestmentsByStatus(@Param("statuses") List<String> statuses,
			@Param("mobileno") long mobileno, Pageable pageable);

	@Query(value = "select inv from Investment inv where inv.dealid = :dealid")
	public Investment getInvDetailsByDealid(@Param("dealid") int dealid);

	@Query("select case when count(inv) > 0 then true else false end from Investment inv where inv.dealid = :dealid and inv.status not in ('CANCELLED')")
	public boolean isInvExistsForDeal(@Param("dealid") int dealid);
	
	@Query("select inv from Investment inv where inv.status in ('ONGOING') and inv.showdate <= :date")
	public List<Investment> getOngoingInvBeforeDate(@Param("date") LocalDate date);
	
	@Query("select inv from Investment inv where inv.movieid=:movieid")
	public List<Investment> getInvestmentsByMovie(@Param("movieid") int movieid);
}