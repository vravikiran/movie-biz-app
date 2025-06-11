package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class DealRequestObj implements Serializable {
	private static final long serialVersionUID = 1L;
	int movieid;
	LocalDate date;
	int city_id;

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getMovieid() {
		return movieid;
	}

	public LocalDate getDate() {
		return date;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city_id, date, movieid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DealRequestObj other = (DealRequestObj) obj;
		return city_id == other.city_id && Objects.equals(date, other.date) && movieid == other.movieid;
	}

	public DealRequestObj(int movieid, LocalDate date, int city_id) {
		super();
		this.movieid = movieid;
		this.date = date;
		this.city_id = city_id;
	}
}