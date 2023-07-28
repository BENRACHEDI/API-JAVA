package com.gidoc.pdf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer  {

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/pdf2/**")
                .allowedOrigins("http://localhost:4200") // Adjust the allowed origins as needed
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
      }
}
