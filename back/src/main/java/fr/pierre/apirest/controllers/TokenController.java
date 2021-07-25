package fr.pierre.apirest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.pierre.apirest.entities.User;
import fr.pierre.apirest.services.UserService;
import fr.pierre.apirest.util.JwtUtil;

@RestController
@RequestMapping(value = "/token")
public class TokenController {

	@Autowired
	UserService userService;
	@Autowired
	JwtUtil jwtUtil;

    /**
     * Envoi un token avec l'id d'un utilisateur et son mot de passe.
     **/
	@GetMapping("/authentication/{id}")
	public String generateToken(@PathVariable Long id, @RequestHeader("password")String password) throws Exception {
		User user =  userService.getById(id);
		if (user.getPassword().equals(password)) {
			return jwtUtil.generateToken(user.getEmail());
		}
		return null;
	}
}
