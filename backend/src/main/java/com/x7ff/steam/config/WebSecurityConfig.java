package com.x7ff.steam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final static String ADMIN_ROLE = "ADMIN";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// pages that require authorization
		// http.authorizeRequests().antMatchers("/authed").hasAuthority(ADMIN_ROLE)

		// permit all non-configured requests
		http.authorizeRequests().anyRequest().permitAll();

		// login
		// http.formLogin().loginPage("/login").permitAll();

		// logout
		// http.logout().logoutUrl("/logout").permitAll();

		http.headers().cacheControl().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");
	}

}