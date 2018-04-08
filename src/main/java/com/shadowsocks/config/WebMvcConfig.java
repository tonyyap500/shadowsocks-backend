package com.shadowsocks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginIntercepter())
				.addPathPatterns("/user/logout")
				.addPathPatterns("/user/userCenter")
				.addPathPatterns("/user/invite/**")
				.addPathPatterns("/server/**")
				.addPathPatterns("/pay/**")
				.excludePathPatterns("/pay/callback");
		super.addInterceptors(registry);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		super.configureMessageConverters(converters);
	}

}
