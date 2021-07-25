package fr.pierre.apirest.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.pierre.apirest.entities.Book;
import fr.pierre.apirest.repositories.BookRepository;
import fr.pierre.apirest.repositories.CopyRepository;

@Service
public class BookService {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	CopyRepository copyRepository;
	
	Logger logger = LoggerFactory.getLogger(BookService.class);
	
	public Book getByIbn(Long ibn) {
		this.logger.debug("getByIbn Call = " + ibn);
		Book book = bookRepository.findById(ibn).get();
		this.logger.debug("getByIbn Return = " + book);
		return book;
	}
	
	public List<Book> getByAuthorOrTitle(String author, String title) {
		this.logger.debug("getByAuthorAndTitle Call = " + author + " " + title);
		List<Book> books = bookRepository.findByAuthorOrTitle(author, title);
		this.logger.debug("getByAuthorAndTitle Return = " + books);
		return books;
	}

	public Book save(Book book) {
		this.logger.debug("save Call = " + book);
		Book bookreturn = bookRepository.save(book);
		this.logger.debug("save Return = " + bookreturn);
		return bookreturn;
	}
	
	public void delete(Long ibn) {
		this.logger.debug("delete Call = " + ibn);
		bookRepository.deleteById(ibn);
	}

	public List<Book> findAll() {
		List<Book> books = bookRepository.findAll();
		this.logger.debug("findAll Return = " + books);
		return books;
	}
	
	public List<Book> findByRequestsNotNull() {
		List<Book> books = bookRepository.findByRequestsNotNull();
		this.logger.debug("findAll Return = " + books);
		return books;
	}
	
	public Book create(Book book) {
		book.setCopies(null);
		Book book1 = bookRepository.save(book);
		return book1;
	}
}
