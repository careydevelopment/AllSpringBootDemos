package com.careydevelopment.demo.braintreedemo.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.braintreegateway.BraintreeGateway;
import com.careydevelopment.demo.braintreedemo.util.BraintreeGatewayFactory;

@Configuration
@ComponentScan("com.braintreegateway")
public class BraintreeConfig {
    
    private static String CONFIG_FILENAME = "braintree-config.properties";

    @Bean
    public BraintreeGateway getBraintreeGateway() {
        Map<String,String> configMap = getMap();        
        
        BraintreeGateway gateway = null;
        
        try {
            gateway = BraintreeGatewayFactory.fromConfigMapping(configMap);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        return gateway;
    }
    
    
    /**
     * Creates the map that we'll use to initialize the gateway
     * Reads values from the config file
     */
    private Map<String,String> getMap() {
        Map<String,String> map = new HashMap<String,String>();
        
        try {
            //get the input stream from the file in the resources folder
            Resource resource = new ClassPathResource(CONFIG_FILENAME);
            InputStream is = resource.getInputStream();
            
            //create a Properties object of key/value pairs
            Properties properties = new Properties();
            properties.load(is);
            
            //Special thanks to Java 8
            map.putAll(properties.entrySet().stream()
                    .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString())));            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        return map;
    }
}
