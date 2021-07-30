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
		this.logger.info("getByIbn Call = " + ibn);
		Book book = bookRepository.findById(ibn).get();
		this.logger.info("getByIbn Return = " + book);
		return book;
	}
	
	public List<Book> getByAuthorOrTitle(String author, String title) {
		this.logger.info("getByAuthorAndTitle Call = " + author + " " + title);
		List<Book> books = bookRepository.findByAuthorOrTitle(author, title);
		this.logger.info("getByAuthorAndTitle Return = " + books);
		return books;
	}

	public Book save(Book book) {
		book.setCopies(null);
		this.logger.info("save Call = " + book);
		Book bookreturn = bookRepository.save(book);
		this.logger.info("save Return = " + bookreturn);
		return bookreturn;
	}
	
	public Book update(Book book) {
		this.logger.info("update Call = " + book);
		Book bookReturn = bookRepository.save(book);
		this.logger.info("update Return = " + bookReturn);
		return bookReturn;
	}
	
	public void delete(Long ibn) {
		this.logger.debug("delete Call = " + ibn);
		bookRepository.deleteById(ibn);
	}

	public List<Book> findAll() {
		List<Book> books = bookRepository.findAll();
		this.logger.info("findAll Return = " + books);
		return books;
	}
	
	public List<Book> findByRequestsNotNull() {
		List<Book> books = bookRepository.findByRequestsNotNull();
		this.logger.info("findAll Return = " + books);
		return books;
	}
}
