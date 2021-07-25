package fr.pierre.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.pierre.apirest.entities.Book;
import fr.pierre.apirest.entities.InitBook;
import fr.pierre.apirest.entities.Request;
import fr.pierre.apirest.entities.User;
import fr.pierre.apirest.services.BookService;
import fr.pierre.apirest.services.RequestService;
import fr.pierre.apirest.services.UserService;

@RestController
@RequestMapping(value = "/request")
public class RequestController {

	@Autowired
	RequestService requestService;
	@Autowired
	BookService bookService;
	@Autowired
	UserService userService;
	
	InitBook init = new InitBook();

    /**
     * Envoi toutes les listes d'attente.
     **/
	@GetMapping("/")
	public ResponseEntity<List<Request>> getAll() {
		List<Request> requests = requestService.getAll();
		requests.forEach(r -> r.setBook(new Book(r.getBook().getIbn(), r.getBook().getTitle(), r.getBook().getAuthor(), r.getBook().getPublisher())));
		requests.forEach(r -> r.setUser(new User(r.getUser().getId(), r.getUser().getEmail(), r.getUser().getUsername())));
		return ResponseEntity.ok(requests);
	}

    /**
     * Envoi la liste d'attente d'un livre avec l'Ibn.
     **/
	@GetMapping("/book/{ibn}")
	public ResponseEntity<List<Request>> getByBook(@PathVariable Long ibn) {
		List<Request> requests = requestService.getByBookIbn(ibn);
		if (requests != null) {
			requests.forEach(r -> r.setBook(new Book(r.getBook().getIbn(), r.getBook().getTitle(), r.getBook().getAuthor(), r.getBook().getPublisher())));
			requests.forEach(r -> r.setUser(new User(r.getUser().getId(), r.getUser().getEmail(), r.getUser().getUsername())));
			return ResponseEntity.ok(requests);
		}
		return ResponseEntity.badRequest().build();
	}

    /**
     * Envoi la liste d'attente d'un utilisateur avec l'Id.
     **/
	@GetMapping("/user/{id}")
	public ResponseEntity<List<Request>> getByUser(@PathVariable Long id) {
		List<Request> requests = requestService.getByUserId(id);
		if (requests != null) {
			requests.forEach(r -> r.setBook(new Book(r.getBook().getIbn(), r.getBook().getTitle(), r.getBook().getAuthor(), r.getBook().getPublisher())));
			requests.forEach(r -> r.setUser(new User(r.getUser().getId(), r.getUser().getEmail(), r.getUser().getUsername())));
			return ResponseEntity.ok(requests);
		}
		return ResponseEntity.badRequest().build();
	}

    /**
     * Ajoute un utilisateur à la liste d'attente d'un livre.
     **/
	@PutMapping("/add")
	public ResponseEntity<Request> addRequest(@RequestBody Request request) {

		Request requestReturn = requestService.add(request);
		if (requestReturn != null) {
			requestReturn.setBook(new Book(requestReturn.getBook().getIbn(), requestReturn.getBook().getTitle(), requestReturn.getBook().getAuthor(), requestReturn.getBook().getPublisher()));
			requestReturn.setUser(new User(requestReturn.getUser().getId(), requestReturn.getUser().getEmail(), requestReturn.getUser().getUsername()));
			return ResponseEntity.ok(requestReturn);
		}
		return ResponseEntity.badRequest().build();
	}

    /**
     * Met à jour la liste d'attente d'un livre avec l'Id.
     **/
	@PostMapping("/update/{id}")
	public ResponseEntity<Request> updateRequest(@PathVariable Long id, @RequestBody Request request) {
		Request requestById = requestService.getbyId(id);
		if (requestById != null && request.getId() == requestById.getId()) {
			return ResponseEntity.ok(requestService.add(request));
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Supprime un utilisateur à la liste d'attente d'un livre avec l'Id.
     **/
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
		requestService.delete(id);
		return ResponseEntity.ok().build();
	}
}
