package fr.pierre.apirest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.pierre.apirest.entities.Role;
import fr.pierre.apirest.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;

	Logger logger = LoggerFactory.getLogger(RoleService.class);
	
	public Role findByName(String name) {
		this.logger.info("findByName Call = " + name);
		Role role = roleRepository.findByName(name);
		this.logger.info("findByName Return = " + role);
		return role;
	}
}
