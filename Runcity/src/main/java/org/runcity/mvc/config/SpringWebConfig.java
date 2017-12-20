package org.runcity.mvc.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.runcity.mvc.config.util.UserLocaleChangeInterceptor;
import org.runcity.util.CachedFile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@PropertySource("/WEB-INF/conf/app.properties")
@ComponentScan({ "org.runcity.mvc.web", "org.runcity.mvc.rest", "org.runcity.mvc.validator",
		"org.runcity.mvc.web.filter" })
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringWebConfig extends WebMvcConfigurerAdapter {
	@Resource
	private Environment env;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/")
				.setCachePeriod(new Integer(env.getRequiredProperty("runcity.resource_cache_time")));
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
		rb.setBasenames(new String[] { "/i18n/main" });
		return rb;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver resolver = new SessionLocaleResolver();
		return resolver;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("utf-8");
		commonsMultipartResolver.setMaxUploadSize(50000000);
		return commonsMultipartResolver;
	}

	@Bean
	public Cache<String, CachedFile> fileCache() {
		return new Cache2kBuilder<String, CachedFile>() {}
			.name("fileCache")
			.entryCapacity(100)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.build();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserLocaleChangeInterceptor());
	}
}
