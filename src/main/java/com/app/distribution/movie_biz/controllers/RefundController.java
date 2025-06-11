package com.app.distribution.movie_biz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.distribution.movie_biz.entites.Refund;
import com.app.distribution.movie_biz.entites.RefundInfo;
import com.app.distribution.movie_biz.services.RefundService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Refund transactions", description = "Refunds amount to investors for movies which got postponed/ not released")
@RestController
@RequestMapping("/refund")
public class RefundController {
	@Autowired
	RefundService refundService;

	@Operation(description = "Once the refund amount is transferred to the user, refund status is updated to complete along with transaction details."
			+ " Only Admin or Super Admin can update the refund information")
	@ApiResponse(responseCode = "200", description = "Refund status and information updated successfully")
	@PutMapping("/update/status")
	@PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
	public ResponseEntity<Refund> updateRefundInfoAndStatus(@RequestBody RefundInfo refundInfo) {
		Refund refund = refundService.updateRefundInfoAndStatus(refundInfo);
		return ResponseEntity.ok(refund);
	}
}
