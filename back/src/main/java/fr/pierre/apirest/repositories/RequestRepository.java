package fr.pierre.apirest.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.pierre.apirest.entities.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

	public List<Request> findByBookIbn(Long ibn);
	
	public List<Request> findByUserId(Long id);
}
