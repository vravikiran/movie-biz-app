package com.app.distribution.movie_biz.entites;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class MovieDealCountPojo implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	String language;
	LocalTime duration;
	String cbfcrating;
	LocalDate releasedate;
	String imageurl;
	String formattype;
	List<String> genre;
	int count;
	int movieid;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public LocalTime getDuration() {
		return duration;
	}

	public void setDuration(LocalTime duration) {
		this.duration = duration;
	}

	public String getCbfcrating() {
		return cbfcrating;
	}

	public void setCbfcrating(String cbfcrating) {
		this.cbfcrating = cbfcrating;
	}

	public LocalDate getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(LocalDate releasedate) {
		this.releasedate = releasedate;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getFormattype() {
		return formattype;
	}

	public void setFormattype(String formattype) {
		this.formattype = formattype;
	}

	public List<String> getGenre() {
		return genre;
	}

	public void setGenre(List<String> genre) {
		this.genre = genre;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getMovieid() {
		return movieid;
	}

	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cbfcrating, count, duration, formattype, genre, imageurl, language, movieid, name,
				releasedate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovieDealCountPojo other = (MovieDealCountPojo) obj;
		return Objects.equals(cbfcrating, other.cbfcrating) && count == other.count
				&& Objects.equals(duration, other.duration) && Objects.equals(formattype, other.formattype)
				&& Objects.equals(genre, other.genre) && Objects.equals(imageurl, other.imageurl)
				&& Objects.equals(language, other.language) && movieid == other.movieid
				&& Objects.equals(name, other.name) && Objects.equals(releasedate, other.releasedate);
	}

	public MovieDealCountPojo() {
		super();
	}

}