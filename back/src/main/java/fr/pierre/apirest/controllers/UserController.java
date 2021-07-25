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

import fr.pierre.apirest.entities.User;
import fr.pierre.apirest.services.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	UserService userService;

    /**
     * Envoi la liste des utilisateurs.
     **/
	@GetMapping("/")
	public ResponseEntity<List<User>> getAll() {
		List<User> users = userService.findAll();
		users.forEach(user->user.setBookings(null));
		for (int i = 0; i < users.size(); i++) {
			users.get(i).getRoles().forEach(user->user.setUsers(null));
		}
		return ResponseEntity.ok(userService.findAll());
	}

    /**
     * Envoi un utilisateur avec l'Id.
     **/
	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable Long id) {
		User userById = userService.getById(id);
		if (userById != null) {
			for (int i = 0; i < userById.getRoles().size(); i++) {
				userById.getRoles().get(i).setUsers(null);
			}
			for (int j = 0; j < userById.getBookings().size(); j++) {
				userById.getBookings().get(j).setUser(null);
				userById.getBookings().get(j).setCopy(null);;
			}
			return ResponseEntity.ok(userById);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Envoi un utilisateur avec l'Email.
     **/
	@GetMapping("/find/{email}")
	public ResponseEntity<User> getByEmail(@PathVariable String email) {
		User userByEmail = userService.findByEmail(email);
		if (userByEmail != null) {
			userByEmail.setBookings(null);
			userByEmail.getRoles().forEach(user->user.setUsers(null));
			userByEmail.setRequests(null);
			return ResponseEntity.ok(userByEmail);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Envoi un utilisateur avec l'Username.
     **/
	@GetMapping("/name/{name}")
	public ResponseEntity<User>getByName(@PathVariable String name) {
		User userByName = userService.getByName(name);
		if (userByName != null) {
			userByName.setBookings(null);
			userByName.getRoles().forEach(user->user.setUsers(null));
			return ResponseEntity.ok(userByName);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Sauvegarde un utilisateur.
     **/
	@PutMapping("/save")
	public ResponseEntity<User> saveUser(@RequestBody User user) {
		return ResponseEntity.ok(userService.save(user));
	}

    /**
     * Met à jour un utilisateur avec l'Id.
     **/
	@PostMapping("/update/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
		User userById = userService.getById(id);
		if (userById != null && user.getId() == userById.getId()) {
			return ResponseEntity.ok(userService.save(userById));
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Supprime un utilisateur avec l'Id.
     **/
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteById(id);
		return ResponseEntity.ok().build();
	}
}
