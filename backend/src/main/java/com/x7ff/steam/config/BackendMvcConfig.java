package com.x7ff.steam.config;

import javax.inject.Inject;

import com.x7ff.steam.shared.config.MvcConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class BackendMvcConfig extends WebMvcConfigurationSupport {
	private static final int ONE_YEAR = 31556926;

	@Inject
	private MvcConfig mvcConfig;

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
				.resourceChain(mvcConfig.cacheResources())
				.addResolver(resourceResolver)
				.addTransformer(new AppCacheManifestTransformer());
	}

}