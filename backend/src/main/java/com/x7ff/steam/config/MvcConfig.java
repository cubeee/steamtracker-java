package com.x7ff.steam.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.x7ff.steam.shared.util.annotation.CacheableKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {
	private static final int ONE_YEAR = 31556926;

	@Inject
	private Environment environment;

	@Inject
	private RequestMappingHandlerAdapter adapter;

	@PostConstruct
	public void removeRedirectModelAtttributes() {
		adapter.setIgnoreDefaultModelOnRedirect(true);
	}

	@Bean
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		return new ResourceUrlEncodingFilter();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
		c.setIgnoreUnresolvablePlaceholders(true);
		return c;
	}

	@Bean(name = CacheableKeyGenerator.NAME)
	public CacheableKeyGenerator cacheableKeyGenerator() {
		return new CacheableKeyGenerator();
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DomainInterceptor());
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

	boolean cacheResources() {
		return !environment.acceptsProfiles("development");
	}

}