package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Table(name = "transaction_details")
@Entity
public class Transaction {
	@Id
	private String transaction_id;
	@Nonnull
	private String status;
	@Nonnull
	private LocalDate transaction_date;
	@NotNull(message = "investment id cannot be null")
	private String investment_id;
	private double amount;

	public Transaction() {
		super();
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

	public LocalDate getTransaction_date() {
		return transaction_date;
	}

	public void setTransaction_date(LocalDate transaction_date) {
		this.transaction_date = transaction_date;
	}

	public String getInvestment_id() {
		return investment_id;
	}

	public void setInvestment_id(String investment_id) {
		this.investment_id = investment_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, investment_id, status, transaction_date, transaction_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaction other = (Transaction) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(investment_id, other.investment_id) && Objects.equals(status, other.status)
				&& Objects.equals(transaction_date, other.transaction_date)
				&& Objects.equals(transaction_id, other.transaction_id);
	}

}