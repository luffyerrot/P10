package fr.pierre.api.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.pierre.api.entities.Request;
import fr.pierre.api.entities.User;
import fr.pierre.api.services.BookingService;
import fr.pierre.api.services.RequestService;
import fr.pierre.api.services.UserService;


@Controller
@RequestMapping(value = "/request")
public class RequestController {

	@Autowired
	public UserService serviceUser;
	@Autowired
	public BookingService serviceBooking;
	@Autowired
	public RequestService serviceRequest;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView create(ModelMap model) {
		User user = serviceUser.userAuth();
		List<Request> requests = serviceRequest.getRequestByUserId(user.getId());
		model.addAttribute("requests", requests);
		if (requests != null) {
			List<Date> dates = new ArrayList<>();
			for (int i = 0; i < requests.size(); i++) {
				if (serviceBooking.getFirstDate(requests.get(i).getBook().getIbn()) != null) {
					dates.add(serviceBooking.getFirstDate(requests.get(i).getBook().getIbn()));
				} else {
					dates.add(null);
				}
			}
			model.addAttribute("firstdates", dates);
		}
	    return new ModelAndView("request/home", model);
	}
}
