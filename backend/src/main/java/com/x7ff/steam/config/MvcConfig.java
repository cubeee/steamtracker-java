package com.x7ff.steam.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	private static final int ONE_YEAR = 31556926;

	@Inject
	private Environment environment;

	@Inject
	private RequestMappingHandlerAdapter adapter;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}

	@PostConstruct
	public void removeRedirectModelAtttributes() {
		adapter.setIgnoreDefaultModelOnRedirect(true);
	}

	@Bean
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		return new ResourceUrlEncodingFilter();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		VersionResourceResolver resourceResolver = new VersionResourceResolver();
		resourceResolver.addVersionStrategy(new ContentVersionStrategy(), "/**");

		registry.addResourceHandler("/**")
				.addResourceLocations(
						"classpath:/static/",
						"classpath:/templates/"
				)
				.setCachePeriod(ONE_YEAR)
				.resourceChain(cacheResources())
				.addResolver(resourceResolver)
				.addTransformer(new AppCacheManifestTransformer());
	}

	private boolean cacheResources() {
		return !environment.acceptsProfiles("development");
	}

}