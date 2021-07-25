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
import fr.pierre.api.services.TokenService;
import fr.pierre.api.entities.Book;

@Controller
@RequestMapping(value = "/book")
public class BookController {
	
	@Autowired
	public BookService serviceBook;
	@Autowired
	public TokenService tokenService;
	@Autowired
	public CopyService serviceCopy;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getBookByIbn(ModelMap model) {
	    model.addAttribute("books", serviceBook.getAllBook());
	    return new ModelAndView("book/home", model);
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public ModelAndView postBookByIbn(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn) {
	    model.addAttribute("book", serviceBook.getBookByIbn(ibn));
	    if (serviceBook.getBookByIbn(ibn).getCopies() != null) {
		    model.addAttribute("copyNum", serviceBook.getBookByIbn(ibn).getCopies().size());
	    } else {
		    model.addAttribute("copyNum", 0);
	    }
	    model.addAttribute("copies", serviceBook.getBookByIbn(ibn).getCopies());
	    return new ModelAndView("book/detail", model);
	}
	
	@RequestMapping(value = "/admin/create", method = RequestMethod.GET)
	public ModelAndView create(ModelMap model) {
	    return new ModelAndView("book/create", model);
	}
	
	@RequestMapping(value = "/admin/create", method = RequestMethod.POST)
	public ModelAndView create(ModelMap model, @ModelAttribute("book") Book book, @RequestParam(name="date", required = true) String date) {
		serviceBook.create(book, date);
		model.addAttribute("books", serviceBook.getAllBook());
	    return new ModelAndView("redirect:/book/", model);
	}
	
	@RequestMapping(value = "/admin/update", method = RequestMethod.GET)
	public ModelAndView update(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn) {
	    model.addAttribute("bookIbn", serviceBook.getBookByIbn(ibn));
	    return new ModelAndView("book/update", model);
	}
	
	@RequestMapping(value = "/admin/update", method = RequestMethod.POST)
	public ModelAndView update(ModelMap model, @ModelAttribute("book") Book book) {
		Book bookNotChange = serviceBook.getBookByIbn(book.getIbn());
		book.setCopies(bookNotChange.getCopies());
		book.setRelease_date(bookNotChange.getRelease_date());
		serviceBook.update(book, book.getIbn());
	    model.addAttribute("books", serviceBook.getAllBook());
	    return new ModelAndView("redirect:/book/", model);
	}
	
	@RequestMapping(value = "/admin/delete", method = RequestMethod.GET)
	public ModelAndView delete(ModelMap model, @RequestParam(name="ibn", required = true) Long ibn) {
		Book book = serviceBook.getBookByIbn(ibn);
		for (int i =0; i < book.getCopies().size(); i++) {
			serviceCopy.delete(book.getCopies().get(i).getId());
		}
		serviceBook.delete(ibn);
	    model.addAttribute("books", serviceBook.getAllBook());
	    return new ModelAndView("redirect:/book/", model);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(ModelMap model, @RequestParam(name="author", required = true) String author, @RequestParam(name="title", required = true) String title) {
		if (!author.isEmpty() || !title.isEmpty()) {
		    model.addAttribute("books", serviceBook.getByAuthorOrTitle(author, title));
		} else {
			model.addAttribute("books", serviceBook.getAllBook());
		}
	    return new ModelAndView("book/home", model);
	}
}
