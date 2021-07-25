package fr.pierre.apirest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.pierre.apirest.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	public Role findByName(String name);
}