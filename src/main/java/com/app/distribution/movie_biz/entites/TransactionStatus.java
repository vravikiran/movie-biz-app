package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.util.Objects;

public class TransactionStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private String status;
	private String transactionId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, transactionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionStatus other = (TransactionStatus) obj;
		return Objects.equals(status, other.status) && Objects.equals(transactionId, other.transactionId);
	}

	public TransactionStatus() {
		super();
	}

}