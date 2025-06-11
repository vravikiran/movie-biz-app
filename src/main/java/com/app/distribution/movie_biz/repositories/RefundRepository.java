package com.app.distribution.movie_biz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.distribution.movie_biz.entites.Refund;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
	@Query(value="select refund from Refund refund where refund.investment_id=:investment_id")
	public Refund getRefundDetailsByInvestmentId(@Param("investment_id") String investment_id);
}
