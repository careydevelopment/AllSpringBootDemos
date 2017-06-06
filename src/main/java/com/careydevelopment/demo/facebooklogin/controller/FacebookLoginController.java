package com.careydevelopment.demo.facebooklogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FacebookLoginController {
	
    //starting page for Facebook login demo
    @RequestMapping("/facebookLogin")
    public String facebookLogin(Model model) {
        return "facebookLogin";
    }
}
