package com.app.distribution.movie_biz.entites;

import java.util.Objects;

public class DealIdAndDealPrice {
	private int dealid;
	private double total_dealprice;

	public int getDealid() {
		return dealid;
	}

	public void setDealid(int dealid) {
		this.dealid = dealid;
	}

	public double getTotal_dealprice() {
		return total_dealprice;
	}

	public void setTotal_dealprice(double total_dealprice) {
		this.total_dealprice = total_dealprice;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dealid, total_dealprice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DealIdAndDealPrice other = (DealIdAndDealPrice) obj;
		return dealid == other.dealid
				&& Double.doubleToLongBits(total_dealprice) == Double.doubleToLongBits(other.total_dealprice);
	}

	@Override
	public String toString() {
		return "DealPrice [dealid=" + dealid + ", total_dealprice=" + total_dealprice + "]";
	}

	public DealIdAndDealPrice() {
		super();
	}

}