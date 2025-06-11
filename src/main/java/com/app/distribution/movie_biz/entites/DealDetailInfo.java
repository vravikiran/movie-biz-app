package com.app.distribution.movie_biz.entites;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DealDetailInfo {
	 String getMoviename();
	 int getMovieid();
	 LocalDate getMoviereleasedate();
	 int getTheatreid();
	 String getTheatrename();
	 LocalDate getShowdate();
	 LocalTime getShowtime();
	 double getTotaldealprice();
	 int getCapacity();
}