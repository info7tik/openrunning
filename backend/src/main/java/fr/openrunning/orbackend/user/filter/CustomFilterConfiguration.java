package fr.openrunning.orbackend.user.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomFilterConfiguration implements WebMvcConfigurer {
    @Autowired
    private TokenCheckerFilter tokenFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenFilter)
                .addPathPatterns("/**").excludePathPatterns("/user/sign*", "/user/test");
    }
}