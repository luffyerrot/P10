package fr.pierre.apirest.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.pierre.apirest.entities.Book;
import fr.pierre.apirest.entities.InitBook;
import fr.pierre.apirest.entities.User;
import fr.pierre.apirest.services.BookService;
import fr.pierre.apirest.services.CopyService;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	BookService bookService;
	@Autowired
	CopyService copyService;
	
	InitBook init = new InitBook();

    /**
     * Renvoi la liste de tous les livres.
     **/
	@GetMapping("/")
	public ResponseEntity<List<Book>> getAll() {
		List<Book> books = bookService.findAll();
		for (int i = 0; i < books.size(); i++) {

			for (int j = 0; j < books.get(i).getCopies().size(); j++) {
				books.get(i).getCopies().get(j).setBook(null);
				books.get(i).getCopies().get(j).setBookings(null);
			}
			for (int k = 0; k < books.get(i).getRequests().size(); k++) {
				books.get(i).getRequests().get(k).setBook(null);
				books.get(i).getRequests().get(k).setUser(null);
			}
		}
		return ResponseEntity.ok(books);
	}
	
    /**
     * Renvoi un livre avec son Ibn.
     **/
	@GetMapping("/{ibn}")
	public ResponseEntity<Book> getByIbn(@PathVariable Long ibn) {
		Book bookByIbn = bookService.getByIbn(ibn);
		if (bookByIbn != null) {
			for (int i = 0; i < bookByIbn.getCopies().size(); i++) {
				bookByIbn.getCopies().get(i).setBook(null);
				bookByIbn.getCopies().get(i).setBookings(null);
			}
			for (int j = 0; j < bookByIbn.getRequests().size(); j++) {
				bookByIbn.getRequests().get(j).setBook(null);
				bookByIbn.getRequests().get(j).setUser(null);
			}
			return ResponseEntity.ok(bookByIbn);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Renvoi tous les livres qui ont une liste d'attente.
     **/
	@GetMapping("/requestnotnull")
	public ResponseEntity<List<Book>> getByRequestId() {
		List<Book> booksByRequestId = bookService.findByRequestsNotNull();
		if (booksByRequestId != null) {
			List<Book> booksReturn = new ArrayList<>();
			for (int i = 0; i < booksByRequestId.size(); i++) {
				
				booksByRequestId.get(i).getCopies().forEach(c -> c.setBook(new Book(c.getBook().getIbn(), c.getBook().getTitle(), c.getBook().getAuthor(), c.getBook().getPublisher())));
				booksByRequestId.get(i).getCopies().forEach(c -> c.setBookings(null));
				
				booksByRequestId.get(i).getRequests().forEach(r -> r.setBook(new Book(r.getBook().getIbn(), r.getBook().getTitle(), r.getBook().getAuthor(), r.getBook().getPublisher())));
				booksByRequestId.get(i).getRequests().forEach(r -> r.setUser(new User(r.getUser().getId(), r.getUser().getEmail(), r.getUser().getUsername())));
				if (!booksReturn.contains(booksByRequestId.get(i))) {
					booksReturn.add(booksByRequestId.get(i));
				}
			}	
			
			return ResponseEntity.ok(booksReturn);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Sauvegarde un livre et le renvoi.
     **/
	@PutMapping("/save")
	public ResponseEntity<Book> saveBook(@RequestBody Book book) {

		Book book1 = bookService.save(book);
		return ResponseEntity.ok(book1);
	}

    /**
     * Met à jour un livre avec sont Ibn.
     **/
	@PostMapping("/update/{ibn}")
	public ResponseEntity<Book> updateBook(@PathVariable Long ibn, @RequestBody Book book) {
		Book bookByIbn = bookService.getByIbn(ibn);
		if (bookByIbn != null && book.getIbn() == bookByIbn.getIbn()) {
			return ResponseEntity.ok(bookService.save(book));
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Supprime un livre avec sont Ibn.
     **/
	@DeleteMapping("/delete/{ibn}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long ibn) {
		bookService.delete(ibn);
		return ResponseEntity.ok().build();
	}

    /**
     * Envoi la liste des livres avec l'auteur et/ou le titre.
     **/
	@GetMapping("/search")
	public ResponseEntity<List<Book>> getByAuthorOrTitle(@RequestHeader("author") String author, @RequestHeader("title") String title) {

		List<Book> books = bookService.getByAuthorOrTitle(author, title);
		if (books != null) {
			for (int i = 0; i < books.size(); i++) {
				for (int j = 0; j < books.get(i).getCopies().size(); j++) {
					books.get(i).getCopies().get(j).setBook(null);
					books.get(i).getCopies().get(j).setBookings(null);
				}
				for (int k = 0; k < books.get(i).getRequests().size(); k++) {
					books.get(i).getRequests().get(k).setBook(null);
					books.get(i).getRequests().get(k).setUser(null);
				}
			}
			return ResponseEntity.ok(books);
		}
		return ResponseEntity.notFound().build();
	}
}