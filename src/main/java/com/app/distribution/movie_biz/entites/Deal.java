package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Deal implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dealid;
	private int theatre_id;
	private int movieid;
	private LocalDate showdate;
	private LocalTime showtime;
	private double total_dealprice;
	private double total_actualprice;
	private String actual_ticket_price;
	private double maxprofit;
	private int city_id;
	private boolean is_invested;
	@JsonIgnore
	private LocalDate created_date;
	@JsonIgnore
	private LocalDate updated_date;
	
	public String getActual_ticket_price() {
		return actual_ticket_price;
	}

	public void setActual_ticket_price(String actual_ticket_price) {
		this.actual_ticket_price = actual_ticket_price;
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

	public boolean isIs_invested() {
		return is_invested;
	}

	public void setIs_invested(boolean is_invested) {
		this.is_invested = is_invested;
	}

	public int getDealid() {
		return dealid;
	}

	public void setDealid(int dealid) {
		this.dealid = dealid;
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

	public double getTotal_dealprice() {
		return total_dealprice;
	}

	public void setTotal_dealprice(double total_dealprice) {
		this.total_dealprice = total_dealprice;
	}

	public double getTotal_actualprice() {
		return total_actualprice;
	}

	public void setTotal_actualprice(double total_actualprice) {
		this.total_actualprice = total_actualprice;
	}

	public double getMaxprofit() {
		return maxprofit;
	}

	public void setMaxprofit(double maxprofit) {
		this.maxprofit = maxprofit;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public Deal() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(city_id, created_date, dealid, is_invested, maxprofit, movieid, showdate,
				showtime, theatre_id, total_actualprice, total_dealprice, updated_date);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Deal other = (Deal) obj;
		return city_id == other.city_id && Objects.equals(created_date, other.created_date) && dealid == other.dealid
				&& is_invested == other.is_invested
				&& Double.doubleToLongBits(maxprofit) == Double.doubleToLongBits(other.maxprofit)
				&& movieid == other.movieid
				&& Objects.equals(showdate, other.showdate) && Objects.equals(showtime, other.showtime)
				&& theatre_id == other.theatre_id
				&& Double.doubleToLongBits(total_actualprice) == Double.doubleToLongBits(other.total_actualprice)
				&& Double.doubleToLongBits(total_dealprice) == Double.doubleToLongBits(other.total_dealprice)
				&& Objects.equals(updated_date, other.updated_date);
	}

	@Override
	public String toString() {
		return "Deal [dealid=" + dealid + ", theatre_id=" + theatre_id + ", movieid=" + movieid + ", showdate="
				+ showdate + ", showtime=" + showtime + ", total_dealprice=" + total_dealprice + ", total_actualprice="
				+ total_actualprice + ", maxprofit=" + maxprofit
				+ ", city_id=" + city_id + ", is_invested=" + is_invested + ", created_date=" + created_date
				+ ", updated_date=" + updated_date + "]";
	}

}