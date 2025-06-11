package com.app.distribution.movie_biz.entites;

import java.util.Objects;

public class PaymentDetails {
	private String merchantId;
	private String merchantTransactionId;
	private String transactionId;
	private double amount;
	private String state;
	private String responseCode;
	private PaymentInstrument paymentInstrument;
	private FeesContext feesContext;

	public FeesContext getFeesContext() {
		return feesContext;
	}

	public void setFeesContext(FeesContext feesContext) {
		this.feesContext = feesContext;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantTransactionId() {
		return merchantTransactionId;
	}

	public void setMerchantTransactionId(String merchantTransactionId) {
		this.merchantTransactionId = merchantTransactionId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public PaymentInstrument getPaymentInstrument() {
		return paymentInstrument;
	}

	public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
		this.paymentInstrument = paymentInstrument;
	}

	public PaymentDetails() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, merchantId, merchantTransactionId, paymentInstrument, responseCode, state,
				transactionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentDetails other = (PaymentDetails) obj;
		return Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(merchantId, other.merchantId)
				&& Objects.equals(merchantTransactionId, other.merchantTransactionId)
				&& Objects.equals(paymentInstrument, other.paymentInstrument)
				&& Objects.equals(responseCode, other.responseCode) && Objects.equals(state, other.state)
				&& Objects.equals(transactionId, other.transactionId);
	}

	@Override
	public String toString() {
		return "PaymentDetails [merchantId=" + merchantId + ", merchantTransactionId=" + merchantTransactionId
				+ ", transactionId=" + transactionId + ", amount=" + amount + ", state=" + state + ", responseCode="
				+ responseCode + ", paymentInstrument=" + paymentInstrument + "]";
	}

}