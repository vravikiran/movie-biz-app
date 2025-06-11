package com.app.distribution.movie_biz.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum InvestmentStatusEnum {
	PROCESSING,ONGOING,COMPLETED,CANCELLED;
	public static Map<String,InvestmentStatusEnum> invStatusValues() {
		return Stream.of(values()).collect(Collectors.toMap(InvestmentStatusEnum::name,Function.identity()));
	}
}