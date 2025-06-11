package com.app.distribution.movie_biz.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TransactionStatusEnum {
	FAILED, COMPLETED,PENDING;

	public static Map<String, TransactionStatusEnum> transactionStatusValues() {
		return Stream.of(values()).collect(Collectors.toMap(TransactionStatusEnum::name, Function.identity()));
	}
}