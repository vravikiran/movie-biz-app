package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Refund {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long refundid;
	private LocalDate created_date;
	private LocalDate updated_date;
	private String transaction_id;
	private String status;
	private String investment_id;
	private double refund_amount;
	private long mobileno;

	public long getRefundid() {
		return refundid;
	}

	public void setRefundid(long refundid) {
		this.refundid = refundid;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInvestment_id() {
		return investment_id;
	}

	public void setInvestment_id(String investment_id) {
		this.investment_id = investment_id;
	}

	public double getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(double refund_amount) {
		this.refund_amount = refund_amount;
	}

	public long getMobileno() {
		return mobileno;
	}

	public void setMobileno(long mobileno) {
		this.mobileno = mobileno;
	}

	@Override
	public int hashCode() {
		return Objects.hash(created_date, investment_id, mobileno, refund_amount, refundid, status, transaction_id,
				updated_date);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Refund other = (Refund) obj;
		return Objects.equals(created_date, other.created_date) && Objects.equals(investment_id, other.investment_id)
				&& Objects.equals(mobileno, other.mobileno)
				&& Double.doubleToLongBits(refund_amount) == Double.doubleToLongBits(other.refund_amount)
				&& refundid == other.refundid && Objects.equals(status, other.status)
				&& Objects.equals(transaction_id, other.transaction_id)
				&& Objects.equals(updated_date, other.updated_date);
	}
}
