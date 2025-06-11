package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import com.app.distribution.movie_biz.enums.LanguageEnum;
import com.app.distribution.movie_biz.enums.MovieGenreEnum;
import com.app.distribution.movie_biz.util.EnumValidator;
import com.app.distribution.movie_biz.util.IsValidCbfcRating;
import com.app.distribution.movie_biz.util.IsValidMovieFormat;
import com.app.distribution.movie_biz.util.MultiValueEnumValidator;
import com.app.distribution.movie_biz.util.PatchableObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.annotation.Nonnull;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer" })
public class Movie extends PatchableObject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int movieid;
	@Nonnull
	private String name;
	@EnumValidator(enumClazz = LanguageEnum.class, message = "language is missing/Not a valid language")
	private String language;
	@Nonnull
	private LocalTime duration;
	@Nonnull
	@IsValidCbfcRating(message = "cbfc rating is missing/Not a valid cbfc rating")
	private String cbfc_rating;
	@Nonnull
	private LocalDate release_date;
	private String imageurl;
	@IsValidMovieFormat(message = "movie format is missing/Not a valid movie format")
	private String format_type;
	@MultiValueEnumValidator(enumClazz = MovieGenreEnum.class, message = "One or more genre(s) are invalid")
	@ElementCollection
	@CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movieid"))
	@Column(name = "genre")
	private List<String> genre;
	@JsonIgnore
	private boolean isactive = true;

	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public int getMovieid() {
		return movieid;
	}

	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}

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

	public String getCbfc_rating() {
		return cbfc_rating;
	}

	public void setCbfc_rating(String cbfc_rating) {
		this.cbfc_rating = cbfc_rating;
	}

	public LocalDate getRelease_date() {
		return release_date;
	}

	public void setRelease_date(LocalDate release_date) {
		this.release_date = release_date;
	}

	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getFormat_type() {
		return format_type;
	}

	public void setFormat_type(String format_type) {
		this.format_type = format_type;
	}

	public List<String> getGenre() {
		return genre;
	}

	public void setGenre(List<String> genre) {
		this.genre = genre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cbfc_rating, duration, format_type, genre, imageurl, language, movieid, name, release_date,
				isactive);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movie other = (Movie) obj;
		return Objects.equals(cbfc_rating, other.cbfc_rating) && Objects.equals(duration, other.duration)
				&& Objects.equals(format_type, other.format_type) && Objects.equals(genre, other.genre)
				&& Objects.equals(imageurl, other.imageurl) && Objects.equals(language, other.language)
				&& movieid == other.movieid && Objects.equals(name, other.name)
				&& Objects.equals(isactive, other.isactive) && Objects.equals(release_date, other.release_date);
	}

	public Movie() {
		super();
	}

}