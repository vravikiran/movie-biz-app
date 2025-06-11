package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.util.List;

public class DealResult implements Serializable {
	private static final long serialVersionUID = 1L;
	List<Deal> deals;
	int count;
	String propertyName;
	String theatreName;
	int theatreCapacity;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public List<Deal> getDeals() {
		return deals;
	}

	public void setDeals(List<Deal> deals) {
		this.deals = deals;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public int getTheatreCapacity() {
		return theatreCapacity;
	}

	public void setTheatreCapacity(int theatreCapacity) {
		this.theatreCapacity = theatreCapacity;
	}
}