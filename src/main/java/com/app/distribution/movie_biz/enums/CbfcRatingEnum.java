package com.app.distribution.movie_biz.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CbfcRatingEnum {
	U("U"), U_A("U/A"), A("A");

	private String name;

	private CbfcRatingEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static Map<String, CbfcRatingEnum> cbfcValues() {
		return Stream.of(values()).collect(Collectors.toMap(CbfcRatingEnum::getName,Function.identity()));
	}
}