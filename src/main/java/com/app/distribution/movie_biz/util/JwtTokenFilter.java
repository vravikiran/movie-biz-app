package com.app.distribution.movie_biz.util;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.app.distribution.movie_biz.enums.TokenTypeEnum;
import com.app.distribution.movie_biz.services.UserProfileService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	@Autowired
	JwtHelper jwtHelper;
	@Autowired
	UserProfileService userProfileService;
	@Autowired
	@Qualifier("handlerExceptionResolver")
	HandlerExceptionResolver exceptionResolver;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException, UsernameNotFoundException {
		String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		String tokenType = request.getHeader(Constants.TOKEN_TYPE);
		boolean isTokenValid = false;
		try {
			if (authToken != null && tokenType != null) {
				UserDetails userDetails = null;

				if (tokenType.toUpperCase().equals(TokenTypeEnum.EMAIL.name())
						&& jwtHelper.vaildateEmailToken(authToken)) {
					String email = jwtHelper.extractEmail(authToken);
					userDetails = userProfileService.loadUserByEmail(email);
					isTokenValid = true;
				} else if (tokenType.toUpperCase().equals(TokenTypeEnum.MOBILENO.name())
						&& jwtHelper.vaildateToken(authToken)) {
					String mobileno = jwtHelper.extractMobileNo(authToken);
					userDetails = userProfileService.loadUserByUsername(mobileno);
					isTokenValid = true;
				}
				if (isTokenValid) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource());
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
			filterChain.doFilter(request, response);
		} catch (UsernameNotFoundException exception) {
			exceptionResolver.resolveException(request, response, null, exception);
		}
	}
}