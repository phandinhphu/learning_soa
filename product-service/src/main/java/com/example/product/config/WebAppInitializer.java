package com.example.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.*;

@Configuration
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebConfig.class };
	}
	
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
	