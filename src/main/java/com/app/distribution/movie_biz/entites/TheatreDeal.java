package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class TheatreDeal implements Serializable {
	private static final long serialVersionUID = 1L;
	Movie movie;
	List<Deal> deals;

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public List<Deal> getDeals() {
		return deals;
	}

	public void setDeals(List<Deal> deals) {
		this.deals = deals;
	}

	public TheatreDeal() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(deals, movie);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TheatreDeal other = (TheatreDeal) obj;
		return Objects.equals(deals, other.deals) && Objects.equals(movie, other.movie);
	}
}