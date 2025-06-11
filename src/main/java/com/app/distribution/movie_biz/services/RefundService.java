package com.app.distribution.movie_biz.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.distribution.movie_biz.entites.Refund;
import com.app.distribution.movie_biz.entites.RefundInfo;
import com.app.distribution.movie_biz.enums.RefundStatusEnum;
import com.app.distribution.movie_biz.repositories.RefundRepository;

@Service
public class RefundService {
	@Autowired
	RefundRepository refundRepository;

	/**
	 * on cancellation of investment, refund is initiated
	 * @param refund
	 * @return
	 */
	public Refund initiateRefund(Refund refund) {
		refund.setCreated_date(LocalDate.now());
		refund.setUpdated_date(LocalDate.now());
		return refundRepository.save(refund);
	}
	
	/**
	 * once the refund is settled, updates refund status and information regarding refund
	 * @param refundInfo
	 * @return
	 */
	public Refund updateRefundInfoAndStatus(RefundInfo refundInfo) {
		Refund refund = refundRepository.getRefundDetailsByInvestmentId(refundInfo.getInvestmentId());
		refund.setUpdated_date(LocalDate.now());
		refund.setTransaction_id(refundInfo.getTransactionId());
		refund.setStatus(RefundStatusEnum.COMPLETED.name());
		return refundRepository.save(refund);
	}
}
