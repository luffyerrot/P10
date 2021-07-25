package fr.pierre.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.pierre.api.services.BookService;
import fr.pierre.api.services.BookingService;
import fr.pierre.api.services.CopyService;
import fr.pierre.api.services.RequestService;
import fr.pierre.api.services.UserService;
import fr.pierre.api.entities.User;

@Controller
@RequestMapping(value = "/booking")
public class BookingController {

	@Autowired
	public UserService serviceUser;
	@Autowired
	public BookingService serviceBooking;
	@Autowired
	public BookService serviceBook;
	@Autowired
	public CopyService serviceCopie;
	@Autowired
	public RequestService serviceRequest;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getBookByIbn(ModelMap model) {
	    model.addAttribute("books", serviceBook.getAllBook());
	    return new ModelAndView("booking/home", model);
	}
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView getAllBooking(ModelMap model) {
	    model.addAttribute("bookingsnotaccepteds", serviceBooking.getAllBookingNotRenderedAndAcceptedNull());
	    model.addAttribute("bookings", serviceBooking.getAllBookingNotRenderedAndAccepted(true));
	    model.addAttribute("bookingshistoriques", serviceBooking.getAllBookingRendered());
	    model.addAttribute("bookingsdenieds", serviceBooking.getAllBookingNotRenderedAndAccepted(false));
	    return new ModelAndView("booking/adminhome", model);
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ModelAndView detail(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn) {
		model.addAttribute("book", serviceBook.getBookByIbn(ibn));
		model.addAttribute("userbookingcopy", serviceBooking.userBookingCopyId());
		model.addAttribute("copies", serviceBook.getBookByIbn(ibn).getCopies());
		model.addAttribute("full", serviceCopie.freeForBooking(ibn));
		model.addAttribute("ibn", ibn);
		model.addAttribute("requests", serviceRequest.getRequestByBookIbn(ibn));
		model.addAttribute("requestuserids", serviceRequest.getIdByRequestUser(ibn));
		model.addAttribute("firstdate", serviceBooking.getFirstDate(ibn));
		model.addAttribute("userid", serviceUser.userAuth().getId());
		return new ModelAndView("booking/detail", model);
	}
	
	@RequestMapping(value = "/admin/pret", method = RequestMethod.GET)
	public ModelAndView pret(ModelMap model, @RequestParam(name="id", required = true) Long id) {
		model.addAttribute("copie", serviceCopie.getCopyId(id));
		return new ModelAndView("booking/pret", model);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn) {
		serviceBooking.create(ibn, serviceUser.userAuth().getEmail());
	    model.addAttribute("bookings", serviceBooking.getAllBookingNotRendered());
	    model.addAttribute("bookingshistoriques", serviceBooking.getAllBookingRendered());
	    return new ModelAndView("redirect:/booking/", model);
	}
	
	@RequestMapping(value = "/extend", method = RequestMethod.GET)
	public ModelAndView extend(ModelMap model, @RequestParam(name="id", required = true)Long id, @ModelAttribute("user") User user) {
		serviceBooking.extend(id);
	    model.addAttribute("bookings", serviceBooking.getByUserIdAndAcceptedAndUserclaim("true"));
	    model.addAttribute("bookingsNotAccepteds", serviceBooking.getByUserIdAndAcceptedAndUserclaim("null"));
	    return new ModelAndView("redirect:/", model);
	}
	
	@RequestMapping(value = "/userclaim", method = RequestMethod.GET)
	public ModelAndView userClaim(ModelMap model, @RequestParam(name="id", required = true)Long id, @RequestParam(name="userclaim", required = true)Boolean userclaim, @ModelAttribute("user") User user) {
		serviceBooking.userClaim(id, userclaim);
	    model.addAttribute("bookings", serviceBooking.getByUserIdAndAcceptedAndUserclaim("true"));
	    model.addAttribute("bookingsNotAccepteds", serviceBooking.getByUserIdAndAcceptedAndUserclaim("null"));
	    return new ModelAndView("redirect:/", model);
	}
	
	@RequestMapping(value = "/admin/rendering", method = RequestMethod.GET)
	public ModelAndView rendered(ModelMap model, @RequestParam(name="id", required = true) Long id) {
		serviceBooking.changeRendering(id);
	    model.addAttribute("bookingsnotaccepteds", serviceBooking.getAllBookingNotRenderedAndAcceptedNull());
	    model.addAttribute("bookings", serviceBooking.getAllBookingNotRenderedAndAccepted(true));
	    model.addAttribute("bookingshistoriques", serviceBooking.getAllBookingRendered());
	    model.addAttribute("bookingsdenieds", serviceBooking.getAllBookingNotRenderedAndAccepted(false));
	    return new ModelAndView("redirect:/booking/admin", model);
	}
	
	@RequestMapping(value = "/admin/accepted", method = RequestMethod.GET)
	public ModelAndView accepted(ModelMap model, @RequestParam(name="id", required = true) Long id, @RequestParam(name="accepted", required = true) String accepted) {
		serviceBooking.changeAccepted(id, Boolean.parseBoolean(accepted));
	    model.addAttribute("bookingsnotaccepteds", serviceBooking.getAllBookingNotRenderedAndAcceptedNull());
	    model.addAttribute("bookings", serviceBooking.getAllBookingNotRenderedAndAccepted(true));
	    model.addAttribute("bookingshistoriques", serviceBooking.getAllBookingRendered());
	    model.addAttribute("bookingsdenieds", serviceBooking.getAllBookingNotRenderedAndAccepted(false));
	    return new ModelAndView("redirect:/booking/admin", model);
	}
	
	@RequestMapping(value = "/admin/request", method = RequestMethod.GET)
	public ModelAndView request(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn) {
		model.addAttribute("ibn", ibn);
		model.addAttribute("requests", serviceRequest.getRequestByBookIbn(ibn));
	    return new ModelAndView("/booking/request", model);
	}
	
	@RequestMapping(value = "/request/create", method = RequestMethod.GET)
	public ModelAndView requestCreate(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn, @RequestParam(name="email", required = false) String email) {
		Boolean admin = false;
		if (email == null) {
			email = serviceUser.userAuth().getEmail();
		} else {
			admin = true;
		}
		serviceBooking.requestCreate(ibn, email);
		model.addAttribute("book", serviceBook.getBookByIbn(ibn));
		model.addAttribute("userbookingcopy", serviceBooking.userBookingCopyId());
		model.addAttribute("copies", serviceBook.getBookByIbn(ibn).getCopies());
		model.addAttribute("full", serviceCopie.freeForBooking(ibn));
		model.addAttribute("ibn", ibn);
		model.addAttribute("requests", serviceRequest.getRequestByBookIbn(ibn));
		model.addAttribute("requestuserids", serviceRequest.getIdByRequestUser(ibn));
		model.addAttribute("firstdate", serviceBooking.getFirstDate(ibn));
		model.addAttribute("userid", serviceUser.userAuth().getId());
	    if (admin) {
			return new ModelAndView("redirect:/booking/admin/request", model);
	    }
		return new ModelAndView("redirect:/booking/detail", model);
	}
	

	@RequestMapping(value = "/request/delete", method = RequestMethod.GET)
	public ModelAndView requestDelete(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn, @RequestParam(name="email", required = false) String email) {
		Boolean admin = false;
		if (email == null) {
			email = serviceUser.userAuth().getEmail();
		} else {
			admin = true;
		}
		serviceBooking.requestDelete(ibn, email);
		model.addAttribute("book", serviceBook.getBookByIbn(ibn));
		model.addAttribute("userbookingcopy", serviceBooking.userBookingCopyId());
		model.addAttribute("copies", serviceBook.getBookByIbn(ibn).getCopies());
		model.addAttribute("full", serviceCopie.freeForBooking(ibn));
		model.addAttribute("ibn", ibn);
		model.addAttribute("requests", serviceRequest.getRequestByBookIbn(ibn));
		model.addAttribute("requestuserids", serviceRequest.getIdByRequestUser(ibn));
		model.addAttribute("firstdate", serviceBooking.getFirstDate(ibn));
		model.addAttribute("userid", serviceUser.userAuth().getId());
	    if (admin) {
			return new ModelAndView("redirect:/booking/admin/request", model);
	    }
		return new ModelAndView("redirect:/booking/detail", model);
	}
}
