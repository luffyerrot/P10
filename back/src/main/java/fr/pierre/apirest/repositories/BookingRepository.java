package fr.pierre.apirest.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import fr.pierre.apirest.entities.Booking;

@Transactional
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	@Modifying
	@Query("UPDATE Booking b SET b.delay = :delay WHERE b.id = :id")
	public void changeDelay(@Param("id")Long id, @Param("delay")Boolean delay);

	@Modifying
	@Query("UPDATE Booking b SET b.userclaim = :userclaim WHERE b.id = :id")
	public void changeUserClaim(@Param("id")Long id, @Param("userclaim")Boolean userclaim);
	
	@Modifying
	@Query("UPDATE Booking b SET b.rendering = :rendering WHERE b.id = :id")
	public void changeRendering(@Param("id")Long id, @Param("rendering")Boolean rendering);

	@Modifying
	@Query("UPDATE Booking b SET b.accepted = :accepted WHERE b.id = :id")
	public void changeAccepted(@Param("id")Long id, @Param("accepted")Boolean accepted);

	@Modifying
	@Query("UPDATE Booking b SET b.booking_date = :date WHERE b.id = :id")
	public void extend(@Param("id")Long id, @Param("date")Date date);
	
	@Query("SELECT b FROM Booking b WHERE b.booking_date < :actualDate AND b.rendering = false")
	public List<Booking> getByDate(@Param("actualDate")Date actualDate);
	
	public List<Booking> findByUserIdAndRendering(Long id, Boolean rendering);

	public List<Booking> findByCopyBookIbnAndRendering(Long ibn, Boolean rendering);
	
	public Booking findByUserIdAndCopyBookIbnAndRendering(Long id, Long ibn, Boolean rendering);
	
	public List<Booking> findByUserIdAndRenderingAndAcceptedAndUserclaimNull(Long id, Boolean rendering, Boolean accepted);
	
	public List<Booking> findByUserIdAndRenderingAndAcceptedAndUserclaim(Long id, Boolean rendering, Boolean accepted, Boolean userclaim);
	
	public List<Booking> findByRenderingAndAcceptedNull(Boolean rendering);
	
	public List<Booking> findByRenderingAndAccepted(Boolean rendering, Boolean accepted);
	
	public List<Booking> findByRendering(Boolean rendering);
	
	public List<Booking> findByCopyId(Long idCopy);
}
