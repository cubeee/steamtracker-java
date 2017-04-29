package com.x7ff.steam.config;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.x7ff.steam.service.SteamUserDetailsService;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.openid.AxFetchListFactory;
import org.springframework.security.openid.OpenID4JavaConsumer;
import org.springframework.security.openid.OpenIDAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Inject
	private SteamUserDetailsService steamUserDetailsService;

	@Inject
	private MvcConfig mvcConfig;

	private final static String ADMIN_ROLE = "ADMIN";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// pages that require authorization
		// http.authorizeRequests().antMatchers("/authed").hasAuthority(ADMIN_ROLE)

		// permit all non-configured requests
		http.authorizeRequests().anyRequest().permitAll();

		if (!mvcConfig.cacheResources()) {
			http.headers().cacheControl().disable();
		}
	}

	@Inject
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		OpenIDAuthenticationProvider provider = new OpenIDAuthenticationProvider();
		provider.setUserDetailsService(steamUserDetailsService);

		auth.authenticationProvider(provider);
	}

	@Bean
	public OpenID4JavaConsumer openID4JavaConsumer() throws ConsumerException {
		ConsumerManager consumerManager = new ConsumerManager();
		AxFetchListFactory attributesToFetchFactory = identifier -> Lists.newArrayList();
		consumerManager.setMaxAssocAttempts(0);
		return new OpenID4JavaConsumer(consumerManager, attributesToFetchFactory);
	}

}