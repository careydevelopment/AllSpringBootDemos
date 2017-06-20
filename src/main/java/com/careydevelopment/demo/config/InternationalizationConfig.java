package com.careydevelopment.demo.config;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class InternationalizationConfig extends WebMvcConfigurerAdapter {
    
    /**
     * Instantiate the appropriate locale resolution strategy
     * @return locale resolver
     */
    @Bean
    public LocaleResolver localeResolver() {
        //for this demo, we'll use a SessionLocaleResolver object
        //as the name implies, it stores locale info in the session
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        
        //default to US locale
        resolver.setDefaultLocale(Locale.US);
        
        //get out
        return resolver;
    }
    
    
    /**
     * This interceptor allows visitors to change the locale on a per-request basis
     * @return a LocaleChangeInterceptor object
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        //instantiate the object with an empty constructor
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        
        //the request param that we'll use to determine the locale
        interceptor.setParamName("lang");
        
        //get out
        return interceptor;
    }
    
    
    /**
     * This is where we'll add the interceptor object 
     * that handles internationalization
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
