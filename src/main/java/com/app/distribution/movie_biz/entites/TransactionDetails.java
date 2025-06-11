package com.app.distribution.movie_biz.entites;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="payment_details")
public class TransactionDetails {
	@Id
	private String transaction_id;
	private String merchant_id;
	private String merchant_transaction_id;
	private double amount;
	private String state;
	private String response_code;
	private String payment_type;
	private String pg_transaction_id;
	private String pg_service_transaction_id;
	private String card_type;
	private String bank_transaction_id;
	private String pg_authorization_code;
	private String bank_id;
	private String brn;
	private String utr;
	private String unmasked_account_number;
	private String arn;
	private String upi_transaction_id;
	private String card_network;
	private String account_type;
	private String ifsc;

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getMerchant_transaction_id() {
		return merchant_transaction_id;
	}

	public void setMerchant_transaction_id(String merchant_transaction_id) {
		this.merchant_transaction_id = merchant_transaction_id;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
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

	public String getResponse_code() {
		return response_code;
	}

	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getPg_transaction_id() {
		return pg_transaction_id;
	}

	public void setPg_transaction_id(String pg_transaction_id) {
		this.pg_transaction_id = pg_transaction_id;
	}

	public String getPg_service_transaction_id() {
		return pg_service_transaction_id;
	}

	public void setPg_service_transaction_id(String pg_service_transaction_id) {
		this.pg_service_transaction_id = pg_service_transaction_id;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getBank_transaction_id() {
		return bank_transaction_id;
	}

	public void setBank_transaction_id(String bank_transaction_id) {
		this.bank_transaction_id = bank_transaction_id;
	}

	public String getPg_authorization_code() {
		return pg_authorization_code;
	}

	public void setPg_authorization_code(String pg_authorization_code) {
		this.pg_authorization_code = pg_authorization_code;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getUtr() {
		return utr;
	}

	public void setUtr(String utr) {
		this.utr = utr;
	}

	public String getUnmasked_account_number() {
		return unmasked_account_number;
	}

	public void setUnmasked_account_number(String unmasked_account_number) {
		this.unmasked_account_number = unmasked_account_number;
	}

	public String getArn() {
		return arn;
	}

	public void setArn(String arn) {
		this.arn = arn;
	}

	public String getUpi_transaction_id() {
		return upi_transaction_id;
	}

	public void setUpi_transaction_id(String upi_transaction_id) {
		this.upi_transaction_id = upi_transaction_id;
	}

	public String getCard_network() {
		return card_network;
	}

	public void setCard_network(String card_network) {
		this.card_network = card_network;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	@Override
	public int hashCode() {
		return Objects.hash(account_type, amount, arn, bank_id, bank_transaction_id, brn, card_network, card_type, ifsc,
				merchant_id, merchant_transaction_id, payment_type, pg_authorization_code, pg_service_transaction_id,
				pg_transaction_id, response_code, state, transaction_id, unmasked_account_number, upi_transaction_id,
				utr);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransactionDetails other = (TransactionDetails) obj;
		return Objects.equals(account_type, other.account_type)
				&& Double.doubleToLongBits(amount) == Double.doubleToLongBits(other.amount)
				&& Objects.equals(arn, other.arn) && Objects.equals(bank_id, other.bank_id)
				&& Objects.equals(bank_transaction_id, other.bank_transaction_id) && Objects.equals(brn, other.brn)
				&& Objects.equals(card_network, other.card_network) && Objects.equals(card_type, other.card_type)
				&& Objects.equals(ifsc, other.ifsc) && Objects.equals(merchant_id, other.merchant_id)
				&& Objects.equals(merchant_transaction_id, other.merchant_transaction_id)
				&& Objects.equals(payment_type, other.payment_type)
				&& Objects.equals(pg_authorization_code, other.pg_authorization_code)
				&& Objects.equals(pg_service_transaction_id, other.pg_service_transaction_id)
				&& Objects.equals(pg_transaction_id, other.pg_transaction_id)
				&& Objects.equals(response_code, other.response_code) && Objects.equals(state, other.state)
				&& Objects.equals(transaction_id, other.transaction_id)
				&& Objects.equals(unmasked_account_number, other.unmasked_account_number)
				&& Objects.equals(upi_transaction_id, other.upi_transaction_id) && Objects.equals(utr, other.utr);
	}
}