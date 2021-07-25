package fr.pierre.apirest.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "requests")
public class Request {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@Column(nullable=true)
	private Boolean accepted;
	
	@Column(nullable=true)
	private Date accepted_time;
	
	@Column(nullable=false)
	private Date release_date;
	
	//---------------------------------------------------------------------------------

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "book_ibn")
    private Book book;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "user_id")
    private User user;
	
    //----------------------------------------------------------------------------------

	public Request() {
	}
	
	public Request(Date release_date) {
		this.release_date = release_date;
	}
	
	public Request(Long id, Date release_date) {
		this.id = id;
		this.release_date = release_date;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public Date getAccepted_time() {
		return accepted_time;
	}

	public void setAccepted_time(Date accepted_time) {
		this.accepted_time = accepted_time;
	}
}
