package com.careydevelopment.demo.facebooklogin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import facebook4j.Facebook;

@Controller
public class FacebookCallbackController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FacebookCallbackController.class); 
    
    //This is where we land when we get back from Facebook
    @RequestMapping("/facebookCallback")
    public String facebookCallback(@RequestParam(value="code", required=true) String oauthCode,
        HttpServletRequest request, HttpServletResponse response, Model model) {
    
        //get the objects from the session
        Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
        
        try {
            facebook.getOAuthAccessToken(oauthCode);
        	
            //store the user name so we can display it on the web page
            model.addAttribute("username", facebook.getName());
            
            return "facebookLoggedIn";
        } catch (Exception e) {
            LOGGER.error("Problem getting token!",e);
            return "redirect:facebookLogin";
        }
    }
}
