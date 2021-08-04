package fr.pierre.apirest.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import fr.pierre.apirest.entities.Booking;
import fr.pierre.apirest.entities.Copy;
import fr.pierre.apirest.entities.User;
import fr.pierre.apirest.services.BookingService;
import fr.pierre.apirest.services.CopyService;

@RestController
@RequestMapping(value = "/booking")
public class BookingController {

	@Autowired
	CopyService copyService;
	@Autowired
	BookingService bookingService;

    /**
     * Envoi la liste de toutes les réservations.
     **/
	@GetMapping("/")
	public ResponseEntity<List<Booking>> getAllNotRendered() {
		return ResponseEntity.ok(bookingService.findAllNotRendered());
	}

    /**
     * Envoi la liste des réservations qui n'ont pas été rendu et qui non pas encore été accepter.
     **/
	@GetMapping("/acceptednull")
	public ResponseEntity<List<Booking>> getAllNotRenderedAndAcceptedNull() {
		return ResponseEntity.ok(bookingService.findAllNotRenderedAndAcceptedNull());
	}

    /**
     * Envoi la liste des réservations qui ont été rendu.
     **/
	@GetMapping("/rendered")
	public ResponseEntity<List<Booking>> getAllRendered() {
		return ResponseEntity.ok(bookingService.findAllRendered());
	}

    /**
     * Envoi une réservations avec sont Id.
     **/
	@GetMapping("/{id}")
	public ResponseEntity<Booking> getById(@PathVariable Long id) {
		Booking bookingById = bookingService.getById(id);
		return ResponseEntity.ok(bookingById);
	}

    /**
     * Envoi la liste des réservations accepté ou refusé avec accepted.
     **/
	@PostMapping("/acceptednotrendered")
	public ResponseEntity<List<Booking>> getAllAndAccepted(@RequestBody String accepted) {
		List<Booking> bookingByUserId = bookingService.findAllNotRenderedAndAccepted(accepted.contains("true") ? true : false);
		if (bookingByUserId != null) {
			bookingByUserId.forEach(booking->booking.setCopy(new Copy(booking.getCopy().getId(), booking.getCopy().getEtat(), booking.getCopy().getBook().getAuthor(), booking.getCopy().getBook().getPublisher(), booking.getCopy().getBook().getTitle(), booking.getCopy().getBook().getIbn())));
			bookingByUserId.forEach(booking->booking.setUser(new User(booking.getUser().getId(), booking.getUser().getEmail(), booking.getUser().getUsername())));
			return ResponseEntity.ok(bookingByUserId);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Envoi la liste des réservations d'un utilisateur avec l'Id.
     **/
	@GetMapping("/userid/{id}")
	public ResponseEntity<List<Booking>> getByUserId(@PathVariable Long id) {
		List<Booking> bookingByUserId = bookingService.getByUserId(id);
		if (bookingByUserId != null) {
			bookingByUserId.forEach(booking->booking.setCopy(new Copy(booking.getCopy().getId(), booking.getCopy().getEtat(), booking.getCopy().getBook().getAuthor(), booking.getCopy().getBook().getPublisher(), booking.getCopy().getBook().getTitle(), booking.getCopy().getBook().getIbn())));
			bookingByUserId.forEach(booking->booking.setUser(new User(booking.getUser().getId(), booking.getUser().getEmail(), booking.getUser().getUsername())));
			return ResponseEntity.ok(bookingByUserId);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Envoi la date la plus ancienne parmi toutes les réservations avec L'Ibn.
     **/
	@GetMapping("/firstdate/{ibn}")
	public ResponseEntity<Date> getFirstDateBooking(@PathVariable Long ibn) {
		List<Booking> bookingByCopyBookIbn = bookingService.getByCopyBookIbn(ibn);
		if (bookingByCopyBookIbn != null && !bookingByCopyBookIbn.isEmpty()) {
			Date date = null;
			Booking firstBooking = null;
			for (int i = 0; i < bookingByCopyBookIbn.size(); i++) {
				if (firstBooking != null) {
					if (!firstBooking.getBooking_date().before(bookingByCopyBookIbn.get(i).getBooking_date())) {
						firstBooking = bookingByCopyBookIbn.get(i);
					}
				} else {
					firstBooking = bookingByCopyBookIbn.get(i);
				}
			}
			if (firstBooking != null) {
				date = firstBooking.getBooking_date();
			}
			
			if (date != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(date);
				c.add(Calendar.DATE, 30);
				date = c.getTime();
				return ResponseEntity.ok(date);
			}
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Envoi la liste des réservations d'un utilisateur qui n'à pas été accepter ou qui a été accepté ou refusé avec L'Id.
     **/
	@PostMapping("/useridandaccepted/{id}")
	public ResponseEntity<List<Booking>> getByUserIdAndAccepted(@PathVariable Long id, @RequestBody String userclaim) {
		List<Booking> bookingByUserId = new ArrayList<>();
		if (userclaim.contains("null")) {
			bookingByUserId = bookingService.getByUserIdAndAcceptedAndClaimNull(id);
		} else {
			bookingByUserId = bookingService.getByUserIdAndAcceptedAndClaim(id, userclaim.contains("true") ? true : false);
		}
		if (bookingByUserId != null) {
			bookingByUserId.forEach(booking->booking.setCopy(new Copy(booking.getCopy().getId(), booking.getCopy().getEtat(), booking.getCopy().getBook().getAuthor(), booking.getCopy().getBook().getPublisher(), booking.getCopy().getBook().getTitle(), booking.getCopy().getBook().getIbn())));
			bookingByUserId.forEach(booking->booking.setUser(new User(booking.getUser().getId(), booking.getUser().getEmail(), booking.getUser().getUsername())));
			return ResponseEntity.ok(bookingByUserId);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Sauvegarde et renvoi une réservation.
     **/
	@PutMapping("/save")
	public void saveBooking(@RequestBody Booking booking) {
		//regarde si la liste des réservations ne contiens pas une réservation avec le même utilisateur et le même livre et qui n'a pas encore été rendu.
		if (bookingService.getByUserIdAndBookIbnNotRendered(booking.getUser().getId(), copyService.getById(booking.getCopy().getId()).getBook().getIbn()) == null) {
			bookingService.save(booking);
		}
	}

    /**
     * Met à jour une réservation avec l'Id.
     **/
	@PostMapping("/update/{id}")
	public ResponseEntity<Void> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
		Booking bookingById = bookingService.getById(id);
		if (bookingById != null && booking.getId() == bookingById.getId()) {
			bookingService.save(bookingById);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Met à jour la variable recall d'une réservation avec l'Id.
     **/
	@PostMapping("/updateRecall/{id}")
	public void updateRecall(@PathVariable Long id, @RequestBody Booking booking) {
		Booking bookingById = bookingService.getById(id);
		if (bookingById != null && booking.getId() == bookingById.getId()) {
			bookingById.setRecall(booking.getRecall());
			bookingService.save(bookingById);
		}
	}

    /**
     * Supprime la réservation avec l'Id.
     **/
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
		bookingService.deleteById(id);
		return ResponseEntity.ok().build();
	}

    /**
     * Envoi la liste des réservations qui on encore du temp avant la fin.
     **/
	@GetMapping("/getdate")
	public ResponseEntity<List<Booking>> getWithDate() {
		List<Booking> bookingByDateAndDelay = bookingService.findAllByDate();
		bookingByDateAndDelay.forEach(booking->booking.setCopy(new Copy(booking.getCopy().getId(), booking.getCopy().getEtat(), booking.getCopy().getBook().getAuthor(), booking.getCopy().getBook().getPublisher(), booking.getCopy().getBook().getTitle(), booking.getCopy().getBook().getIbn())));
		bookingByDateAndDelay.forEach(booking->booking.setUser(new User(booking.getUser().getId(), booking.getUser().getEmail(), booking.getUser().getUsername())));
		return ResponseEntity.ok(bookingByDateAndDelay);
	}
	
	/**
     * Met à jour la variable delay d'une réservation avec l'Id.
     * Ajoute un mois de plus à la réservation initial.
	 * Correction du bug permettant de prolonger une date dépassé.
	 * **/
	@PutMapping("/extend/{id}")
	public void extend(@PathVariable Long id) {
		Booking booking = bookingService.getById(id);
		double hours = ((new Date().getTime() - booking.getBooking_date().getTime()) / (1000 * 60 * 60));
		if (hours < 720) {
			if (booking.getDelay() == false) {
				bookingService.changeDelay(booking, true);
				bookingService.extend(booking);
			}
		}
	}

    /**
     * Met à jour la variable userclaim d'une réservation avec l'Id.
     **/
	@PutMapping("/userclaim/{id}")
	public void userClaim(@PathVariable Long id, @RequestBody String userclaim) {
		Booking booking = bookingService.getById(id);
		if (booking.getUserClaim() == null) {
			bookingService.userclaim(booking, userclaim.contains("true") ? true : false);
		}
	}

    /**
     * Met à jour la variable rendering d'une réservation avec l'Id.
     **/
	@PutMapping("/rendering/{id}")
	public void rendering(@PathVariable Long id) {
		Booking booking = bookingService.getById(id);
		bookingService.changeRendering(booking, true);
	}

    /**
     * Met à jour la variable accepted d'une réservation avec l'Id.
     **/
	@PutMapping("/accepted/{id}")
	public void accepted(@PathVariable Long id, @RequestBody Boolean accepted) {
		Booking booking = bookingService.getById(id);
		bookingService.changeAccepted(booking, accepted);
	}
}
