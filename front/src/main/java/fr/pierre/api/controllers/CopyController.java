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
import fr.pierre.api.services.CopyService;
import fr.pierre.api.entities.Book;
import fr.pierre.api.entities.Copy;

@Controller
@RequestMapping(value = "/copy")
public class CopyController {

	@Autowired
	public BookService serviceBook;
	@Autowired
	public CopyService serviceCopy;

	@RequestMapping(value = "/admin/create", method = RequestMethod.GET)
	public ModelAndView create(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn) {
		model.addAttribute("ibn", ibn);
	    return new ModelAndView("copy/create", model);
	}
	
	@RequestMapping(value = "/admin/create", method = RequestMethod.POST)
	public ModelAndView create(ModelMap model, @ModelAttribute("copy") Copy copy, @RequestParam(name="ibn", required = true) Long ibn) {
		serviceCopy.create(copy, serviceBook.getBookByIbn(ibn));
		model.addAttribute("book", serviceBook.getBookByIbn(ibn));
	    return new ModelAndView("redirect:/book/detail?ibn=" + ibn, model);
	}
	
	@RequestMapping(value = "/admin/update", method = RequestMethod.GET)
	public ModelAndView update(ModelMap model, @RequestParam(name="id", required = true) Long id, @RequestParam(name="ibn", required = true) Long ibn) {
	    model.addAttribute("copyid", serviceCopy.getCopyId(id));
		model.addAttribute("ibn", ibn);
	    return new ModelAndView("copy/update", model);
	}
	
	@RequestMapping(value = "/admin/update", method = RequestMethod.POST)
	public ModelAndView update(ModelMap model, @ModelAttribute("copy") Copy copy, @RequestParam(name="ibn", required = true) Long ibn) {
		Book book = serviceBook.getBookByIbn(ibn);
		copy.setBook(book);
		serviceCopy.update(copy, copy.getId());
	    return new ModelAndView("redirect:/book/detail?ibn=" + ibn, model);
	}
	
	@RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
	public ModelAndView delete(ModelMap model, @RequestParam(name="id", required = true) Long id, @RequestParam(name="ibn", required = true) Long ibn) {
		serviceCopy.delete(id);
		model.addAttribute("book", serviceBook.getBookByIbn(ibn));
	    return new ModelAndView("redirect:/book/detail?ibn=" + ibn, model);
	}
}
