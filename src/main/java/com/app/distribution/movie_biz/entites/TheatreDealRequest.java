package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class TheatreDealRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private int theatre_id;
	private LocalDate date;
	public int getTheatre_id() {
		return theatre_id;
	}
	public void setTheatre_id(int theatre_id) {
		this.theatre_id = theatre_id;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	@Override
	public int hashCode() {
		return Objects.hash(date, theatre_id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TheatreDealRequest other = (TheatreDealRequest) obj;
		return Objects.equals(date, other.date) && theatre_id == other.theatre_id;
	}
	public TheatreDealRequest(int theatre_id, LocalDate date) {
		super();
		this.theatre_id = theatre_id;
		this.date = date;
	}
	public TheatreDealRequest() {
		super();
	}

}