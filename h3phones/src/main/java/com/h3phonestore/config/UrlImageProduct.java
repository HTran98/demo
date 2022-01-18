package com.h3phonestore.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UrlImageProduct implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		Path uploadDir2 = Paths.get("./src/main/resources/static/images/imagesProduct");
		String uploadPath2 = uploadDir2.toFile().getAbsolutePath();
		registry.addResourceHandler("/src/main/resources/static/images/imagesProduct/**").addResourceLocations("file:/"+uploadPath2+"/");
		
	}
}
