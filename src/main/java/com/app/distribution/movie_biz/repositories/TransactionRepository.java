package com.app.distribution.movie_biz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.distribution.movie_biz.entites.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>{

}