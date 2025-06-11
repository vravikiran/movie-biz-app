package com.app.distribution.movie_biz.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.distribution.movie_biz.entites.Role;
import com.app.distribution.movie_biz.exceptions.DuplicateRoleException;
import com.app.distribution.movie_biz.services.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(description = "Allows SUPER_ADMIN to create a new Role to perform/restrict access to the app services", name = "User roles")
@RestController
@RequestMapping("/role")
public class RoleController {
	@Autowired
	RoleService roleService;
	Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Operation(method = "POST", description = "Allows SUPER_ADMIN to create a new Role to perform/restrict access to the app services")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "A Role is created successfully"),
			@ApiResponse(responseCode = "409", description = "A Role with similar name already exists") })
	@PostMapping
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<HttpStatus> createRole(@RequestBody Role role) throws DuplicateRoleException {
		logger.info("Creating a new role with details ::" + role.toString());
		roleService.createRole(role);
		logger.info("Role created successfully");
		return ResponseEntity.ok(HttpStatus.OK);
	}
}