package fr.pierre.apirest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.pierre.apirest.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Modifying
	@Query("UPDATE User u SET u.username = :username, u.email = :email WHERE u.id = :id")
	User updateUser(@Param("id")Long idTopos, @Param("username")String username, @Param("email")String description);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUsername(String name);
}