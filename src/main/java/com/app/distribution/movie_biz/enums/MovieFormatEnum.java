package com.app.distribution.movie_biz.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MovieFormatEnum {
	TWOD("2D"), ICE("ICE"), IMAX_2D("IMAX 2D"), FOURDX("4DX"), THREED("3D"), IMAX_3D("IMAX 3D");

	private String label;

	MovieFormatEnum(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static MovieFormatEnum getEnumValFromLabel(String label) {
		return Stream.of(MovieFormatEnum.values()).filter(movieformat -> movieformat.getLabel().equals(label))
				.findFirst().orElse(null);
	}

	public static Map<String, MovieFormatEnum> movieFormatValues() {
		return Stream.of(values()).collect(Collectors.toMap(MovieFormatEnum::getLabel, Function.identity()));
	}
}