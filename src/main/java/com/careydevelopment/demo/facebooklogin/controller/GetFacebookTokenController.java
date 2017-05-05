package com.careydevelopment.demo.facebooklogin.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;

@Controller
public class GetFacebookTokenController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetFacebookTokenController.class);
	
    @RequestMapping("/getFacebookToken")
    public RedirectView getToken(HttpServletRequest request,Model model) {
    	//this will be the URL that we take the user to
    	String facebookUrl = "";
    	
		try {
			//get the Facebook object
			Facebook facebook = getFacebook();
			
			//get the callback url so they get back here
			String callbackUrl = "http://localhost:8090/facebookCallback";
			
			//let's put Facebook in the session
			request.getSession().setAttribute("facebook", facebook);
			
			//now get the authorization URL from the token
			facebookUrl = facebook.getOAuthAuthorizationURL(callbackUrl);
			
			LOGGER.info("Authorization url is " + facebookUrl);
		} catch (Exception e) {
			LOGGER.error("Problem logging in with Facebook!", e);
		}
    	
		//redirect to the Facebook URL
		RedirectView redirectView = new RedirectView();
	    redirectView.setUrl(facebookUrl);
	    return redirectView;
    }

    
    /*
     * Instantiates the Facebook object
     */
    public Facebook getFacebook() {
    	Facebook facebook = null;
    	
    	//set the consumer key and secret for our app
		String appId = "21686";
		String appSecret = "9acb3f69409f667b9e3";
    	
    	FacebookFactory factory = new FacebookFactory();
    	facebook = factory.getInstance();
    	facebook.setOAuthAppId(appId, appSecret);
    	    	
    	return facebook;
    }
}
