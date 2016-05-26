package com.x7ff.steam.config;

import com.google.common.collect.ImmutableMap;
import kr.pe.kwonnam.freemarker.inheritance.BlockDirective;
import kr.pe.kwonnam.freemarker.inheritance.ExtendsDirective;
import kr.pe.kwonnam.freemarker.inheritance.PutDirective;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class WebFreeMarkerConfiguration extends FreeMarkerAutoConfiguration.FreeMarkerWebConfiguration {

	private static final ImmutableMap<String, Object> DIRECTIVES = ImmutableMap.of(
			"extends", new ExtendsDirective(),
			"block", new BlockDirective(),
			"put", new PutDirective()
	);

	private static final ImmutableMap<String, Object> VARIABLES = ImmutableMap.of("layout", DIRECTIVES);

	@Bean
	@Override
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		applyProperties(configurer);
		configurer.setTemplateLoaderPath("classpath:/templates/");
		configurer.setFreemarkerVariables(VARIABLES);
		return configurer;
	}

}