package com.x7ff.steam.config;

import javax.inject.Inject;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public final class CustomControllerAdvice {

	@Inject
	private Environment environment;

	@ModelAttribute(value = "developmentMode")
	public boolean isDevelopmentMode() {
		return environment.acceptsProfiles("development");
	}

}