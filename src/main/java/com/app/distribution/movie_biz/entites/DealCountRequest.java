package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.util.Objects;

public class DealCountRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String language;
	private int city_id;
	private String name;

	public String getLanguage() {
		return language;
	}

	public int getCity_id() {
		return city_id;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city_id, language, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DealCountRequest other = (DealCountRequest) obj;
		return Objects.equals(city_id, other.city_id) && Objects.equals(language, other.language)
				&& Objects.equals(name, other.name);
	}

	public DealCountRequest(String language, int city_id, String name) {
		super();
		this.language = language;
		this.city_id = city_id;
		this.name = name;
	}

}