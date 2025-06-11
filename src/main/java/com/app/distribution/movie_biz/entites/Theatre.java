package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Theatre implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int theatre_id;
	private int city_id;
	private String location;
	private String property_name;
	private String theatre_name;
	private int capacity;
	private boolean isactive = true;
	@JsonIgnore
	private String mobileno_hash;

	public String getMobileno_hash() {
		return mobileno_hash;
	}

	public void setMobileno_hash(String mobileno_hash) {
		this.mobileno_hash = mobileno_hash;
	}

	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public int getTheatre_id() {
		return theatre_id;
	}

	public void setTheatre_id(int theatre_id) {
		this.theatre_id = theatre_id;
	}

	public int getCity_id() {
		return city_id;
	}

	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getProperty_name() {
		return property_name;
	}

	public void setProperty_name(String property_name) {
		this.property_name = property_name;
	}

	public String getTheatre_name() {
		return theatre_name;
	}

	public void setTheatre_name(String theatre_name) {
		this.theatre_name = theatre_name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Theatre() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(capacity, city_id, isactive, location, property_name, theatre_id, theatre_name,
				mobileno_hash);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Theatre other = (Theatre) obj;
		return capacity == other.capacity && city_id == other.city_id && isactive == other.isactive
				&& Objects.equals(location, other.location) && Objects.equals(property_name, other.property_name)
				&& theatre_id == other.theatre_id && Objects.equals(theatre_name, other.theatre_name)
				&& Objects.equals(mobileno_hash, other.mobileno_hash);
	}
}