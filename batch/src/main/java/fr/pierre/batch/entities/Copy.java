package fr.pierre.batch.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "copies")
public class Copy {
	
	public Copy() {
		
	}
	
	public Copy(Long id, String etat) {
		this.id = id;
		this.etat = etat;
	}
	
	public Copy(Long id, String etat, String author, String publisher, String title, Long ibn) {
		this.id = id;
		this.etat = etat;
		this.book = new Book(ibn, title, author, publisher);
	}
	
	public Copy(String etat) {
		this.etat = etat;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;

	@Column(nullable = false)
	private String etat;
	
	//---------------------------------------------------------------------------------
	   
	@OneToMany(mappedBy = "copy")
    private List <Booking> bookings;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "book_ibn")
    private Book book;
	
    //----------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Copy [id=" + id + ", etat=" + etat + "]";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}
}