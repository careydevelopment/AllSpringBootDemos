package com.careydevelopment.demo.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
    //starting page for Twitter login demo
    @RequestMapping("/")
    public String home(Model model) {
        return "home";
    }
}
