package com.app.distribution.movie_biz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.distribution.movie_biz.entites.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	@Query("select case when count(r) > 0 then true else false end from Role r where upper(r.role_name)= upper(:name)")
	public boolean verifyRoleExists(@Param("name") String name);
}