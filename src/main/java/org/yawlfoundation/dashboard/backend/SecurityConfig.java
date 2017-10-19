/*
 * Copyright (c) 2004-2012 The YAWL Foundation. All rights reserved.
 * The YAWL Foundation is a collaboration of individuals and
 * organisations who are committed to improving workflow technology.
 *
 * This file is part of YAWL. YAWL is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation.
 *
 * YAWL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with YAWL. If not, see <http://www.gnu.org/licenses/>.
 */
package org.yawlfoundation.dashboard.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.yawlfoundation.dashboard.backend.session.RestAuthenticationEntryPoint;
import org.yawlfoundation.dashboard.backend.session.RestAuthenticationFailureHandler;
import org.yawlfoundation.dashboard.backend.session.RestAuthenticationSuccessHandler;
import org.yawlfoundation.dashboard.backend.session.SessionDataHolder;
import org.yawlfoundation.dashboard.backend.session.YawlAuthenticationProvider;
import org.yawlfoundation.dashboard.backend.session.YawlUserDetailsService;



/**
 * The security configuration.
 * @author Philipp R. Thomas <philipp.thomas@floaz.de>
 */
@Configuration
@Order(value = ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
@EnableResourceServer
class SecurityConfig extends ResourceServerConfigurerAdapter {

	@Value("${yawl.authentication.client.id}")
	private String clientId;

	@Value("${yawl.authentication.client.secret}")
	private String clientSecret;

	@Value("${yawl.authentication.check.url}")
	private String tokenCheckUrl;

	@Autowired
	private YawlClientConfig yawlClientConfig;



	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenServices(tokenServices());
	}


	@Bean
	public RemoteTokenServices tokenServices() {
		RemoteTokenServices tokenServices = new RemoteTokenServices();
		tokenServices.setClientId(clientId);
		tokenServices.setClientSecret(clientSecret);
		tokenServices.setCheckTokenEndpointUrl(tokenCheckUrl);
		return tokenServices;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
					.antMatchers("/api/**").authenticated()
					.anyRequest().permitAll()
				.and()
				.exceptionHandling()
					.authenticationEntryPoint(authenticationEntryPoint())
				.and()
				.httpBasic();
	}


	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(yawlAuthenticationProvider());
	}


	@Bean
	protected UserDetailsService userDetailsService() {
		return new YawlUserDetailsService(yawlClientConfig.resourceManager());
	}


	@Bean
	protected YawlAuthenticationProvider yawlAuthenticationProvider() {
		return new YawlAuthenticationProvider(yawlClientConfig.resourceManager(), userDetailsService());
	}


	@Bean
	protected RestAuthenticationEntryPoint authenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}


	@Bean
	protected RestAuthenticationFailureHandler authenticationFailureHandler() {
		return new RestAuthenticationFailureHandler();
	}


	@Bean
	protected RestAuthenticationSuccessHandler authenticationSuccessHandler() {
		return new RestAuthenticationSuccessHandler();
	}

	@Bean
	protected SessionDataHolder sessionDataHolder() {
		return new SessionDataHolder();
	}
}
