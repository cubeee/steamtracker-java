package com.x7ff.steam.shared.config;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.x7ff.steam.shared.util.annotation.CacheableKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

@Configuration
public class MvcConfig extends WebMvcConfigurationSupport {

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

    public boolean cacheResources() {
        return !environment.acceptsProfiles("development");
    }

}