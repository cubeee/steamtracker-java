package com.x7ff.steam.config;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Properties;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.google.common.collect.ImmutableMap;
import com.x7ff.steam.shared.config.MvcConfig;
import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.TemplateException;
import kr.pe.kwonnam.freemarker.inheritance.BlockDirective;
import kr.pe.kwonnam.freemarker.inheritance.ExtendsDirective;
import kr.pe.kwonnam.freemarker.inheritance.PutDirective;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class WebFreeMarkerConfiguration extends WebMvcConfigurerAdapter {

    @Inject
    private MvcConfig mvcConfig;

    private static final ImmutableMap<String, Object> DIRECTIVES = ImmutableMap.of(
            "extends", new ExtendsDirective(),
            "block", new BlockDirective(),
            "put", new PutDirective()
    );

    private static final ImmutableMap<String, Object> VARIABLES = ImmutableMap.of("layout", DIRECTIVES);

    @Bean
    @Inject
    public freemarker.template.Configuration freeMarkerConfig(ServletContext servletContext) throws IOException,
            TemplateException {
        FreeMarkerConfigurer freemarkerConfig = configFreeMarkerConfigurer(servletContext);
        return freemarkerConfig.getConfiguration();
    }

    @Bean
    @Inject
    public TaglibFactory taglibFactory(ServletContext servletContext) throws IOException, TemplateException {
        FreeMarkerConfigurer freemarkerConfig = configFreeMarkerConfigurer(servletContext);
        TaglibFactory taglibFactory = freemarkerConfig.getTaglibFactory();
        taglibFactory.setObjectWrapper(freemarker.template.Configuration.getDefaultObjectWrapper(freemarker.template.Configuration.getVersion()));
        return taglibFactory;
    }

    @Inject
    @Bean
    public FreeMarkerConfig springFreeMarkerConfig(ServletContext servletContext) throws IOException, TemplateException {
        return new MyFreeMarkerConfig(freeMarkerConfig(servletContext), taglibFactory(servletContext));
    }

    private static FreeMarkerConfigurer configFreeMarkerConfigurer(ServletContext servletContext) throws IOException,
            TemplateException {
        FreeMarkerConfigurer freemarkerConfig = new FreeMarkerConfigurer();
        freemarkerConfig
                .setPreTemplateLoaders(new ClassTemplateLoader(WebFreeMarkerConfiguration.class, "/templates/"));
        ServletContext servletContextProxy = (ServletContext) Proxy.newProxyInstance(
                ServletContextResourceHandler.class.getClassLoader(),
                new Class<?>[]{ServletContext.class},
                new ServletContextResourceHandler(servletContext));
        freemarkerConfig.setServletContext(servletContextProxy);
        Properties settings = new Properties();
        settings.put("default_encoding", "UTF-8");
        freemarkerConfig.setFreemarkerSettings(settings);
        freemarkerConfig.setFreemarkerVariables(VARIABLES);
        freemarkerConfig.afterPropertiesSet();
        return freemarkerConfig;
    }

    @Bean
    public FreeMarkerViewResolver viewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setCache(mvcConfig.cacheResources());
        viewResolver.setSuffix(".ftl");
        viewResolver.setContentType("text/html;charset=UTF-8");
        return viewResolver;
    }

    private static class ServletContextResourceHandler implements InvocationHandler {

        private final ServletContext target;

        private ServletContextResourceHandler(ServletContext target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("getResourceAsStream")) {
                Object result = method.invoke(target, args);
                if (result == null) {
                    result = WebFreeMarkerConfiguration.class.getResourceAsStream((String) args[0]);
                }
                return result;
            } else if (method.getName().equals("getResource")) {
                Object result = method.invoke(target, args);
                if (result == null) {
                    result = WebFreeMarkerConfiguration.class.getResource((String) args[0]);
                }
                return result;
            }
            return method.invoke(target, args);
        }
    }

    private static class MyFreeMarkerConfig implements FreeMarkerConfig {

        private final freemarker.template.Configuration configuration;
        private final TaglibFactory taglibFactory;

        private MyFreeMarkerConfig(freemarker.template.Configuration configuration, TaglibFactory taglibFactory) {
            this.configuration = configuration;
            this.taglibFactory = taglibFactory;
        }

        @Override
        public freemarker.template.Configuration getConfiguration() {
            return configuration;
        }

        @Override
        public TaglibFactory getTaglibFactory() {
            return taglibFactory;
        }
    }
}