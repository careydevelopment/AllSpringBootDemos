package com.careydevelopment.demo.braintreedemo.config;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.braintreegateway.BraintreeGateway;
import com.careydevelopment.demo.braintreedemo.util.BraintreeGatewayFactory;

@Configuration
@ComponentScan("com.braintreegateway")
public class BraintreeConfig {
    
    private static String CONFIG_FILENAME = "braintree-config.properties";

    @Bean
    public BraintreeGateway getBraintreeGateway() {
        ClassLoader classLoader = getClass().getClassLoader();
        File configFile = new File(classLoader.getResource(CONFIG_FILENAME).getFile());

        BraintreeGateway gateway = null;
        
        try {
            if(configFile.exists() && !configFile.isDirectory()) {
                gateway = BraintreeGatewayFactory.fromConfigFile(configFile);
            } else {
                gateway = BraintreeGatewayFactory.fromConfigMapping(System.getenv());
            }            
        } catch (NullPointerException e) {
            System.err.println("Could not load Braintree configuration from config file or system environment.");
            System.exit(1);
        }
        
        return gateway;
    }
}
