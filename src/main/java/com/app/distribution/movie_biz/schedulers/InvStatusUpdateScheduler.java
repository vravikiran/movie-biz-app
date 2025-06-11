package com.app.distribution.movie_biz.schedulers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.entites.Investment;
import com.app.distribution.movie_biz.enums.InvestmentStatusEnum;
import com.app.distribution.movie_biz.repositories.InvestmentRepository;

@Service
public class InvStatusUpdateScheduler {
	@Autowired
	InvestmentRepository investmentRepository;

	@Scheduled(cron = "0 0 2 * * 1", zone = "Asia/Kolkata")
	public void invStatusUpdateEveryWeek() {
		List<Investment> investments = investmentRepository.getOngoingInvBeforeDate(LocalDate.now());
		investments.forEach(inv -> {
			inv.setStatus(InvestmentStatusEnum.COMPLETED.name());
			inv.setUpdated_date(LocalDate.now());
		});
		investmentRepository.saveAll(investments);
	}
}
