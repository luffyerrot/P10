package fr.pierre.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.pierre.api.services.BookingService;
import fr.pierre.api.services.UserService;
import fr.pierre.api.entities.User;

@Controller
public class PageController {

	@Autowired
	public UserService serviceUser;
	@Autowired
	public BookingService serviceBooking;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(ModelMap model) {
	    model.addAttribute("bookings", serviceBooking.getByUserIdAndAcceptedAndUserclaim("true"));
	    model.addAttribute("bookingsNotAccepteds", serviceBooking.getByUserIdAndAcceptedAndUserclaim("null"));
	    return new ModelAndView("home", model);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(ModelMap model) {
	    return new ModelAndView("create", model);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(ModelMap model, @ModelAttribute("user") User user) {
		serviceUser.create(user);
	    model.addAttribute("bookings", serviceBooking.getByUserIdAndAcceptedAndUserclaim("true"));
	    model.addAttribute("bookingsNotAccepteds", serviceBooking.getByUserIdAndAcceptedAndUserclaim("null"));
	    return new ModelAndView("redirect:/", model);
	}
}
