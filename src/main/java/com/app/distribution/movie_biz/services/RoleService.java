package com.app.distribution.movie_biz.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.distribution.movie_biz.entites.Role;
import com.app.distribution.movie_biz.exceptions.DuplicateRoleException;
import com.app.distribution.movie_biz.repositories.RoleRepository;

@Service
public class RoleService {
	@Autowired
	RoleRepository roleRepository;
	Logger logger = LoggerFactory.getLogger(RoleService.class);

	/**
	 * creates a role. If a role with given name already exists throws
	 * DuplicateRoleException
	 * 
	 * @param role
	 * @return
	 * @throws DuplicateRoleException
	 */
	@PostMapping
	public Role createRole(Role role) throws DuplicateRoleException {
		logger.info("Started creating a role :: " + role.toString());
		if (!roleRepository.verifyRoleExists(role.getRole_name())) {
			Role createdRole = roleRepository.save(role);
			logger.info("Successfully created a role");
			return createdRole;
		} else {
			throw new DuplicateRoleException("Rolw with given name already exists");
		}
	}
}