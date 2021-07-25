package fr.pierre.apirest.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.pierre.apirest.entities.Copy;

@Repository
public interface CopyRepository extends JpaRepository<Copy, Long> {
	
	public List<Copy> findByBookIbn(Long ibn);
	
	public Optional<Copy> findById(Long id);
}
