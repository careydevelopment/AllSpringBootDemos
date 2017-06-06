package com.careydevelopment.demo.formvalidation.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.careydevelopment.demo.formvalidation.model.Employee;


@Controller
public class FormController {

    //starting page for form validation demo
    @RequestMapping(value = "/form", method=RequestMethod.GET)
    public String form(Model model) {
        //instantiate an employee object
        Employee employee = new Employee();
    	
        //add it to the model
        model.addAttribute("employee", employee);
    
        //get out
        return "form";
    }
    
    
    @RequestMapping(value = "/form", method=RequestMethod.POST)
    public String formSubmit(@Valid Employee employee, BindingResult bindingResult, Model model) {
        //check for errors
        if (bindingResult.hasErrors()) {
            return "form";
        }
    	
        //if there are no errors, show form success screen
        return "formSuccess";
    }
}
