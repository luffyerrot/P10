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
import fr.pierre.apirest.entities.Booking;
import fr.pierre.apirest.entities.Copy;
import fr.pierre.apirest.services.BookService;
import fr.pierre.apirest.services.BookingService;
import fr.pierre.apirest.services.CopyService;

@RestController
@RequestMapping(value = "/copy")
public class CopyController {

	@Autowired
	CopyService copyService;
	@Autowired
	BookingService bookingService;
	@Autowired
	BookService bookService;

    /**
     * Envoi la liste de toutes les copies.
     **/
	@GetMapping("/")
	public ResponseEntity<List<Copy>> getAll() {
		return ResponseEntity.ok(copyService.findAll());
	}

    /**
     * Envoi la copy avec l'Id.
     **/
	@GetMapping("/{id}")
	public ResponseEntity<Copy> getById(@PathVariable Long id) {
		Copy copyById = copyService.getById(id);
		if (copyById != null) {
			copyById.setBook(new Book(copyById.getBook().getIbn(), copyById.getBook().getTitle(), copyById.getBook().getAuthor(), copyById.getBook().getPublisher()));
			copyById.setBookings(null);
		}
		return ResponseEntity.ok(copyById);
	}

    /**
     * Envoi la copy d'un livre avec l'Ibn.
     **/
	@GetMapping("/bookibn/{ibn}")
	public ResponseEntity<List<Copy>> getByBookIbn(@PathVariable Long ibn) {
		List<Copy> copyByBookIbn = copyService.findByBookIbn(ibn);
		if (copyByBookIbn != null) {
			for (int i = 0; i < copyByBookIbn.size(); i++) {
				copyByBookIbn.get(i).setBook(new Book(copyByBookIbn.get(i).getBook().getIbn(), copyByBookIbn.get(i).getBook().getTitle(), copyByBookIbn.get(i).getBook().getAuthor(), copyByBookIbn.get(i).getBook().getPublisher()));
				copyByBookIbn.get(i).setBookings(null);
			}
			return ResponseEntity.ok(copyByBookIbn);
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Sauvegarde et renvoi la copy.
     **/
	@PutMapping("save")
	public ResponseEntity<Copy> saveCopy(@RequestBody Copy copy) {
		return ResponseEntity.ok(copyService.save(copy));
	}

    /**
     * Met à jour la copy avec l'Id.
     **/
	@PostMapping("update/{id}")
	public ResponseEntity<Copy> updateCopy(@PathVariable Long id, @RequestBody Copy copy) {
		Copy copyById = copyService.findById(id);
		if (copyById != null && copy.getId() == copyById.getId()) {
			return ResponseEntity.ok(copyService.update(copy));
		}
		return ResponseEntity.notFound().build();
	}

    /**
     * Supprime la copy avec l'Id.
     **/
	@DeleteMapping("delete/{id}")
	public ResponseEntity<Void> deleteCopy(@PathVariable Long id) {
		copyService.delete(id);
		return ResponseEntity.ok().build();
	}

    /**
     * Envoi true ou false si les copies d'un livre sont toute réservé avec l'Ibn.
     **/
	@GetMapping("/isfull/{ibn}")
	public ResponseEntity<Boolean> freeForBooking(@PathVariable Long ibn) {
		List<Copy> allCopiesByBookIbn = copyService.findByBookIbn(ibn);
		if (allCopiesByBookIbn != null) {
			List<Booking> allBooking = bookingService.findAllNotRendered();
			int count = 0;
			if (allBooking != null) {
				for (int i = 0; i < allCopiesByBookIbn.size(); i++) {
					for (int j = 0; j < allBooking.size(); j++) {
						if (allBooking.get(j).getCopy().getId().equals(allCopiesByBookIbn.get(i).getId())) {
							count++;
						}
					}
				}
			} else {
				return ResponseEntity.notFound().build();
			}
			if (count == allCopiesByBookIbn.size()) {
				return ResponseEntity.ok(true);
			} else {
				return ResponseEntity.ok(false);
			}
		}
		return ResponseEntity.notFound().build();
	}
}