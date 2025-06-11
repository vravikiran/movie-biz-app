package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Investment {
	@Id
	private String investment_id;
	private LocalDate movie_release_date;
	private String movie_name;
	private String theatre_name;
	private LocalDate showdate;
	private LocalTime showtime;
	private String status;
	private double investedamt;
	private double earnedamt = 0;
	private int theatre_id;
	private int movieid;
	private int dealid;
	private int house_capacity;
	private int tickets_sold;
	private LocalDate created_date;
	private LocalDate updated_date;
	private long mobileno;

	private String transaction_id;

	public Investment() {
		super();
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	public int getHouse_capacity() {
		return house_capacity;
	}

	public void setHouse_capacity(int house_capacity) {
		this.house_capacity = house_capacity;
	}

	public int getTickets_sold() {
		return tickets_sold;
	}

	public void setTickets_sold(int tickets_sold) {
		this.tickets_sold = tickets_sold;
	}

	public String getInvestment_id() {
		return investment_id;
	}

	public void setInvestment_id(String investment_id) {
		this.investment_id = investment_id;
	}

	public LocalDate getMovie_release_date() {
		return movie_release_date;
	}

	public void setMovie_release_date(LocalDate movie_release_date) {
		this.movie_release_date = movie_release_date;
	}

	public String getMovie_name() {
		return movie_name;
	}

	public void setMovie_name(String movie_name) {
		this.movie_name = movie_name;
	}

	public String getTheatre_name() {
		return theatre_name;
	}

	public void setTheatre_name(String theatre_name) {
		this.theatre_name = theatre_name;
	}

	public LocalDate getShowdate() {
		return showdate;
	}

	public void setShowdate(LocalDate showdate) {
		this.showdate = showdate;
	}

	public LocalTime getShowtime() {
		return showtime;
	}

	public void setShowtime(LocalTime showtime) {
		this.showtime = showtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getInvestedamt() {
		return investedamt;
	}

	public void setInvestedamt(double investedamt) {
		this.investedamt = investedamt;
	}

	public double getEarnedamt() {
		return earnedamt;
	}

	public void setEarnedamt(double earnedamt) {
		this.earnedamt = earnedamt;
	}

	public int getTheatre_id() {
		return theatre_id;
	}

	public void setTheatre_id(int theatre_id) {
		this.theatre_id = theatre_id;
	}

	public int getMovieid() {
		return movieid;
	}

	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}

	public int getDealid() {
		return dealid;
	}

	public void setDealid(int dealid) {
		this.dealid = dealid;
	}

	public LocalDate getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDate created_date) {
		this.created_date = created_date;
	}

	public LocalDate getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(LocalDate updated_date) {
		this.updated_date = updated_date;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(created_date, dealid, earnedamt, house_capacity, investedamt, investment_id, mobileno,
				movie_name, movie_release_date, movieid, showdate, showtime, status, theatre_id, theatre_name,
				tickets_sold, transaction_id, updated_date);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Investment other = (Investment) obj;
		return Objects.equals(created_date, other.created_date) && dealid == other.dealid
				&& Double.doubleToLongBits(earnedamt) == Double.doubleToLongBits(other.earnedamt)
				&& house_capacity == other.house_capacity
				&& Double.doubleToLongBits(investedamt) == Double.doubleToLongBits(other.investedamt)
				&& Objects.equals(investment_id, other.investment_id) && mobileno == other.mobileno
				&& Objects.equals(movie_name, other.movie_name)
				&& Objects.equals(movie_release_date, other.movie_release_date) && movieid == other.movieid
				&& Objects.equals(showdate, other.showdate) && Objects.equals(showtime, other.showtime)
				&& Objects.equals(status, other.status) && theatre_id == other.theatre_id
				&& Objects.equals(theatre_name, other.theatre_name) && tickets_sold == other.tickets_sold
				&& Objects.equals(transaction_id, other.transaction_id)
				&& Objects.equals(updated_date, other.updated_date);
	}

}