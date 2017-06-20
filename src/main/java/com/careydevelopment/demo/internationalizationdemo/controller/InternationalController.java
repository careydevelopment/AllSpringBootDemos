package com.careydevelopment.demo.internationalizationdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InternationalController {

    @RequestMapping(value = "/international", method=RequestMethod.GET)
    public String international() {
        return "international";
    }
    
}
