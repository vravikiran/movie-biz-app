package com.app.distribution.movie_biz.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.entites.Deal;
import com.app.distribution.movie_biz.entites.DealCountRequest;
import com.app.distribution.movie_biz.entites.DealRequestObj;
import com.app.distribution.movie_biz.entites.DealResult;
import com.app.distribution.movie_biz.entites.Movie;
import com.app.distribution.movie_biz.entites.MovieDealCount;
import com.app.distribution.movie_biz.entites.MovieDealCountPojo;
import com.app.distribution.movie_biz.entites.Theatre;
import com.app.distribution.movie_biz.entites.TheatreDeal;
import com.app.distribution.movie_biz.entites.TheatreDealRequest;
import com.app.distribution.movie_biz.repositories.DealRepository;
import com.app.distribution.movie_biz.repositories.MovieRepository;
import com.app.distribution.movie_biz.repositories.TheatreRepository;

@Service
public class DealService {
	@Autowired
	DealRepository dealRepository;
	@Autowired
	MovieRepository movieRepository;
	@Autowired
	TheatreRepository theatreRepository;
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	Logger logger = LoggerFactory.getLogger(DealService.class);
	private static final Integer EXPIRE_MIN = 5;
	private static final Integer SHOW_ADDL_MINS = 30;

	/**
	 * Allows ADMIN/SUPER_ADMIN to create new deals for a specific theatre and movie
	 * throws DataIntegrityViolationException if deal already exists with given
	 * information
	 * 
	 * @param deal
	 * @return Deal
	 */
	public Deal createDeal(Deal deal) {
		logger.info("Creation of deal started ::" + deal.toString());
		Theatre theatre = theatreRepository.getReferenceById(deal.getTheatre_id());
		deal.setCity_id(theatre.getCity_id());
		deal.setCreated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		deal.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		Deal createdDeal = dealRepository.save(deal);
		logger.info("deal created successfully");
		return createdDeal;
	}

	/**
	 * returns the deals associated with a specific movie. If date is not provided
	 * deals available from current date and time are returned, else returns deals
	 * based on provided date
	 * 
	 * @param dealRequestObj
	 * @param pageable
	 * @return
	 */
	public Page<DealResult> getDealsByMovie(DealRequestObj dealRequestObj, Pageable pageable) {
		logger.info("fetching deals by movie with request parameters :: " + " City :: " + dealRequestObj.getCity_id()
				+ " date " + dealRequestObj.getDate() + " movieid " + dealRequestObj.getMovieid());
		logger.info("pageable parameters :: getDealsByMovie " + " page Number " + pageable.getPageNumber()
				+ " pagination size " + pageable.getPageSize());
		List<DealResult> dealResults = new ArrayList<>();
		Page<DealResult> resultOutput = null;
		if (pageable.getPageNumber() == 0) {
			List<Deal> deals = null;
			if (dealRequestObj.getDate().equals(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate())) {
				deals = dealRepository.getDealsByMovieId(dealRequestObj.getMovieid(), dealRequestObj.getDate(),
						dealRequestObj.getCity_id(), LocalTime.now(ZoneId.of("Asia/Calcutta"))
								.truncatedTo(ChronoUnit.MINUTES).plusMinutes(SHOW_ADDL_MINS));
			} else {
				deals = dealRepository.getDealsByMovieId(dealRequestObj.getMovieid(), dealRequestObj.getDate(),
						dealRequestObj.getCity_id(), null);
			}

			if (!deals.isEmpty()) {
				Map<Integer, List<Deal>> dealsByTheatre = deals.stream()
						.collect(Collectors.groupingBy(Deal::getTheatre_id));

				dealsByTheatre.forEach((key, value) -> {
					DealResult dealResult = new DealResult();
					dealResult.setDeals(value);
					dealResult.setCount(value.size());
					Theatre theatre = theatreRepository.getReferenceById(key);
					dealResult.setPropertyName(theatre.getProperty_name());
					dealResult.setTheatreName(theatre.getTheatre_name());
					dealResult.setTheatreCapacity(theatre.getCapacity());
					dealResults.add(dealResult);
				});
				logger.info("count of deals grouped by movieid :: getDealsByMovie " + dealResults.size());
				resultOutput = pagingDealsByMovie(dealResults, pageable.getPageSize(), pageable.getPageNumber());
			} else {
				logger.info("no deals found with given details :: " + dealRequestObj);
			}
			redisTemplate.opsForValue().set(dealRequestObj, dealResults);
			redisTemplate.expire(dealRequestObj, EXPIRE_MIN, TimeUnit.MINUTES);
		} else {
			@SuppressWarnings("unchecked")
			List<DealResult> cacheDeals = (List<DealResult>) redisTemplate.opsForValue().get(dealRequestObj);
			if (cacheDeals != null) {
				resultOutput = pagingDealsByMovie(cacheDeals, pageable.getPageSize(), pageable.getPageNumber());
			}
		}

		return resultOutput;
	}

	/**
	 * paginates deal results based on a movie and pagination parameters
	 * 
	 * @param list
	 * @param pagesize
	 * @param pageNo
	 * @return
	 */
	private Page<DealResult> pagingDealsByMovie(List<DealResult> list, int pagesize, int pageNo) {

		int totalpages = list.size() / pagesize;
		PageRequest pageable = PageRequest.of(pageNo, pagesize);

		int max = pageNo >= totalpages ? list.size() : pagesize * (pageNo + 1);
		int min = pageNo > totalpages ? max : pagesize * pageNo;

		Page<DealResult> pageResponse = new PageImpl<DealResult>(list.subList(min, max), pageable, list.size());
		return pageResponse;
	}

	/**
	 * paginates MovieDealCount results based on pagination parameters
	 * 
	 * @param list
	 * @param pagesize
	 * @param pageNo
	 * @return
	 */
	private Page<MovieDealCountPojo> toPage(List<MovieDealCountPojo> list, int pagesize, int pageNo) {

		int totalpages = list.size() / pagesize;
		PageRequest pageable = PageRequest.of(pageNo, pagesize);

		int max = pageNo >= totalpages ? list.size() : pagesize * (pageNo + 1);
		int min = pageNo > totalpages ? max : pagesize * pageNo;

		Page<MovieDealCountPojo> pageResponse = new PageImpl<MovieDealCountPojo>(list.subList(min, max), pageable,
				list.size());
		return pageResponse;
	}

	/**
	 * returns the count of deals available for a movie in a given city, date,
	 * langauage If date is not provided returns count of deals for a movie from
	 * current date and time If date is provided, returns count of deals for a movie
	 * on a given date
	 * 
	 * @param dealCountRequest
	 * @param pageable
	 * @return
	 */
	public Page<MovieDealCountPojo> getMovieDealCountByCity(DealCountRequest dealCountRequest, Pageable pageable) {
		logger.info("Fetching number of deals by date and city,langauge, movie name based on request parameters  :: "
				+ dealCountRequest.getCity_id() + "," + dealCountRequest.getName() + ","
				+ dealCountRequest.getLanguage());
		Page<MovieDealCountPojo> pageList = null;
		if (pageable.getPageNumber() == 0) {
			logger.info("local date :: " + ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate().toString()
					+ " Local Time :: "
					+ LocalTime.now(ZoneId.of("Asia/Calcutta")).truncatedTo(ChronoUnit.MINUTES).toString());
			List<MovieDealCount> dealCountByMovieList = dealRepository.getMovieDealCountByCity(
					dealCountRequest.getCity_id(), dealCountRequest.getName(), dealCountRequest.getLanguage(),
					ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate(),
					LocalTime.now(ZoneId.of("Asia/Calcutta")).truncatedTo(ChronoUnit.MINUTES)
							.plusMinutes(SHOW_ADDL_MINS));
			List<MovieDealCountPojo> countPojos = convertMovieDealCountToPojo(dealCountByMovieList);
			pageList = toPage(countPojos, pageable.getPageSize(), pageable.getPageNumber());
			redisTemplate.opsForValue().set(dealCountRequest, countPojos);
			redisTemplate.expire(dealCountRequest, EXPIRE_MIN, TimeUnit.MINUTES);
			logger.info("Total groups by movie :: " + pageList.getTotalElements());
		} else {
			@SuppressWarnings("unchecked")
			List<MovieDealCountPojo> cacheDealCountByMovies = (List<MovieDealCountPojo>) redisTemplate.opsForValue()
					.get(dealCountRequest);
			if (cacheDealCountByMovies != null) {
				pageList = toPage(cacheDealCountByMovies, pageable.getPageSize(), pageable.getPageNumber());
			}
		}
		logger.info("Request to fetch number of deals by date and city,langauge, movie name completed");
		return pageList;
	}

	/**
	 * returns the list of dates where deals are available for a given movie
	 * 
	 * @param movieid
	 * @return
	 */
	public List<LocalDate> getDealDatesByMovie(int movieid) {
		logger.info("Fetching deal dates for a  movie with id :: " + movieid + " date ::"
				+ ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
		return dealRepository.getDealDatesByMovie(movieid, ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
	}

	/**
	 * converts the list of movie deal count to list of movie deal count pojo
	 * 
	 * @param dealCountByMovieList
	 * @return
	 */
	private List<MovieDealCountPojo> convertMovieDealCountToPojo(List<MovieDealCount> dealCountByMovieList) {
		List<MovieDealCountPojo> countPojos = new ArrayList<>();
		MovieDealCountPojo countPojo = null;
		Movie movie = null;
		for (MovieDealCount movieDealCount : dealCountByMovieList) {
			countPojo = new MovieDealCountPojo();
			movie = movieRepository.getReferenceById(movieDealCount.getMovieid());
			countPojo.setCbfcrating(movie.getCbfc_rating());
			countPojo.setCount(movieDealCount.getCount());
			countPojo.setReleasedate(movie.getRelease_date());
			countPojo.setDuration(movie.getDuration());
			countPojo.setFormattype(movie.getFormat_type());
			countPojo.setGenre(movie.getGenre());
			countPojo.setImageurl(movie.getImageurl());
			countPojo.setLanguage(movie.getLanguage());
			countPojo.setMovieid(movie.getMovieid());
			countPojo.setName(movie.getName());
			countPojos.add(countPojo);
		}
		return countPojos;
	}

	/**
	 * updates the deal status
	 * 
	 * @param isActive
	 * @param dealid
	 */
	public void updateDealStatus(boolean isActive, int dealid) {
		logger.info("Updating deal status with id :: " + dealid);
		Deal deal = dealRepository.getReferenceById(dealid);
		if (deal != null) {
			deal.setIs_invested(isActive);
			deal.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			dealRepository.save(deal);
			logger.info("deal status updated successfully");
		} else {
			logger.info("Deal with given ID doesn't exists :: " + dealid);
			throw new NoSuchElementException("Deal with given ID doesn't exists :: " + dealid);
		}
	}

	/**
	 * returns the list of deals available in a given theatre. If a date is provided
	 * returns the results based on given date If date is not provided, returns the
	 * results from current date and time
	 * 
	 * @param dealRequest
	 * @param pageable
	 * @return
	 */
	public Page<TheatreDeal> getDealsByTheatre(TheatreDealRequest dealRequest, Pageable pageable) {
		logger.info("Fetching deals by theatre for a given date :: " + dealRequest.toString());
		Page<TheatreDeal> pageTheatreDeals = null;
		if (pageable.getPageNumber() == 0) {
			List<Deal> deals = null;
			if (dealRequest.getDate().equals(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate())) {
				deals = dealRepository.getDealsByTheatreAndDate(dealRequest.getTheatre_id(), dealRequest.getDate(),
						LocalTime.now(ZoneId.of("Asia/Calcutta")).truncatedTo(ChronoUnit.MINUTES)
								.plusMinutes(SHOW_ADDL_MINS));
			} else {
				deals = dealRepository.getDealsByTheatreAndDate(dealRequest.getTheatre_id(), dealRequest.getDate(),
						null);
			}
			Map<Integer, List<Deal>> dealsByMovie = deals.stream().collect(Collectors.groupingBy(Deal::getMovieid));
			List<TheatreDeal> theatreDeals = convertDealsByMovieMapToTheatreDeals(dealsByMovie);
			pageTheatreDeals = (Page<TheatreDeal>) pagingDealsByTheatre(theatreDeals, pageable.getPageSize(),
					pageable.getPageNumber());
			redisTemplate.opsForValue().set(dealRequest, theatreDeals);
			redisTemplate.expire(dealRequest, EXPIRE_MIN, TimeUnit.MINUTES);
		} else {
			@SuppressWarnings("unchecked")
			List<TheatreDeal> cachedTheatreDeals = (List<TheatreDeal>) redisTemplate.opsForValue().get(dealRequest);
			pageTheatreDeals = (Page<TheatreDeal>) pagingDealsByTheatre(cachedTheatreDeals, pageable.getPageSize(),
					pageable.getPageNumber());
		}
		logger.info("completed fetching deals by theatre with page and size ::" + pageable.toString());
		return pageTheatreDeals;
	}

	/**
	 * Groups deals by movie in a given theatre
	 * 
	 * @param dealsByMovie
	 * @return
	 */
	private List<TheatreDeal> convertDealsByMovieMapToTheatreDeals(Map<Integer, List<Deal>> dealsByMovie) {
		List<TheatreDeal> theatreDeals = new ArrayList<>();
		dealsByMovie.forEach((key, value) -> {
			TheatreDeal theatreDeal = new TheatreDeal();
			Movie movie = movieRepository.getReferenceById(key);
			theatreDeal.setDeals(value);
			theatreDeal.setMovie(movie);
			theatreDeals.add(theatreDeal);
		});
		return theatreDeals;
	}

	/**
	 * provides pagination for theatre deals based on pagination parameters
	 * @param theatreDeals
	 * @param pagesize
	 * @param pageNo
	 * @return
	 */
	private Page<TheatreDeal> pagingDealsByTheatre(List<TheatreDeal> theatreDeals, int pagesize, int pageNo) {

		int totalpages = theatreDeals.size() / pagesize;
		PageRequest pageable = PageRequest.of(pageNo, pagesize);

		int max = pageNo >= totalpages ? theatreDeals.size() : pagesize * (pageNo + 1);
		int min = pageNo > totalpages ? max : pagesize * pageNo;

		Page<TheatreDeal> pageResponse = new PageImpl<TheatreDeal>(theatreDeals.subList(min, max), pageable,
				theatreDeals.size());
		return pageResponse;
	}

	/**
	 * returns the list of dates where deals are available in a given theatre
	 * @param theatre_id
	 * @return
	 */
	public List<LocalDate> getDealDatesByTheatre(int theatre_id) {
		logger.info("fetching deal dates of a theatre with id: " + theatre_id);
		return dealRepository.getDealDatesByTheatre(theatre_id,
				ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
	}

	/**
	 * Updates the price of a deal
	 * @param dealid
	 * @param dealPrice
	 */
	public void updateDealPrice(int dealid, double dealPrice) {
		logger.info("Started updating total deal price for a deal with id :: " + dealid);
		Deal deal = dealRepository.getReferenceById(dealid);
		if (deal != null) {
			deal.setUpdated_date(ZonedDateTime.now(ZoneId.of("Asia/Calcutta")).toLocalDate());
			deal.setTotal_dealprice(dealPrice);
			deal.setMaxprofit(dealPrice);
			dealRepository.save(deal);
			logger.info("Successfully updated total deal price for a deal with id :: " + dealid);
		} else {
			logger.info("Deal with given ID doesn't exists :: " + dealid);
			throw new NoSuchElementException("Deal with given ID doesn't exists :: " + dealid);
		}

	}
}