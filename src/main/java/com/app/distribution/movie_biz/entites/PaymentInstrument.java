package com.app.distribution.movie_biz.entites;

import java.util.Objects;

public class PaymentInstrument {
	private String type;
	private String pgTransactionId;
	private String pgServiceTransactionId;
	private String cardType;
	private String bankTransactionId;
	private String pgAuthorizationCode;
	private String bankId;
	private String brn;
	private String utr;
	private String unmaskedAccountNumber;
	private String arn;
	private String upiTransactionId;
	private String cardNetwork;
	private String accountType;
	private String ifsc;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPgTransactionId() {
		return pgTransactionId;
	}

	public String getUpiTransactionId() {
		return upiTransactionId;
	}

	public void setUpiTransactionId(String upiTransactionId) {
		this.upiTransactionId = upiTransactionId;
	}

	public String getCardNetwork() {
		return cardNetwork;
	}

	public void setCardNetwork(String cardNetwork) {
		this.cardNetwork = cardNetwork;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getIfsc() {
		return ifsc;
	}

	public void setIfsc(String ifsc) {
		this.ifsc = ifsc;
	}

	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}

	public String getPgServiceTransactionId() {
		return pgServiceTransactionId;
	}

	public void setPgServiceTransactionId(String pgServiceTransactionId) {
		this.pgServiceTransactionId = pgServiceTransactionId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBankTransactionId() {
		return bankTransactionId;
	}

	public void setBankTransactionId(String bankTransactionId) {
		this.bankTransactionId = bankTransactionId;
	}

	public String getPgAuthorizationCode() {
		return pgAuthorizationCode;
	}

	public void setPgAuthorizationCode(String pgAuthorizationCode) {
		this.pgAuthorizationCode = pgAuthorizationCode;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
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

	public String getUnmaskedAccountNumber() {
		return unmaskedAccountNumber;
	}

	public void setUnmaskedAccountNumber(String unmaskedAccountNumber) {
		this.unmaskedAccountNumber = unmaskedAccountNumber;
	}

	public String getArn() {
		return arn;
	}

	public void setArn(String arn) {
		this.arn = arn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(arn, bankId, bankTransactionId, brn, cardType, pgAuthorizationCode, pgServiceTransactionId,
				pgTransactionId, type, unmaskedAccountNumber, utr);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentInstrument other = (PaymentInstrument) obj;
		return Objects.equals(arn, other.arn) && Objects.equals(bankId, other.bankId)
				&& Objects.equals(bankTransactionId, other.bankTransactionId) && Objects.equals(brn, other.brn)
				&& Objects.equals(cardType, other.cardType)
				&& Objects.equals(pgAuthorizationCode, other.pgAuthorizationCode)
				&& Objects.equals(pgServiceTransactionId, other.pgServiceTransactionId)
				&& Objects.equals(pgTransactionId, other.pgTransactionId) && Objects.equals(type, other.type)
				&& Objects.equals(unmaskedAccountNumber, other.unmaskedAccountNumber) && Objects.equals(utr, other.utr);
	}

	public PaymentInstrument() {
		super();
	}

	@Override
	public String toString() {
		return "PaymentInstrument [type=" + type + ", pgTransactionId=" + pgTransactionId + ", pgServiceTransactionId="
				+ pgServiceTransactionId + ", cardType=" + cardType + ", bankTransactionId=" + bankTransactionId
				+ ", pgAuthorizationCode=" + pgAuthorizationCode + ", bankId=" + bankId + ", brn=" + brn + ", utr="
				+ utr + ", unmaskedAccountNumber=" + unmaskedAccountNumber + ", arn=" + arn + "]";
	}

}