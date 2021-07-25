package fr.pierre.api.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView create(ModelMap model) {
	    return new ModelAndView("redirect:/", model);
	}
}
