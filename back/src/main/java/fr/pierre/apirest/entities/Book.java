package fr.pierre.apirest.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

	public Book() {
		
	}
	
	public Book(String title, String author, String publisher) {
		this.title = title;
		this.author = author;
		this.publisher = publisher;
	}
	
	public Book(Long ibn, String title, String author, String publisher) {
		this.ibn = ibn;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
	}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ibn")
    private Long ibn;
	
	@Column(nullable=false, unique=false)
	private String title;
	
	@Column(nullable=false)
    private String author;
	
	@Column(nullable=false)
	private String publisher;
	
	@Column(nullable=false)
	private Date release_date;
	
	//---------------------------------------------------------------------------------

	@OneToMany(mappedBy = "book")
    private List<Copy> copies;
	
	@OneToMany(mappedBy = "book")
    private List <Request> requests;
	
    //----------------------------------------------------------------------------------
	

	@Override
	public String toString() {
		return "Book [ibn=" + ibn + ", title=" + title + ", author=" + author + ", publisher=" + publisher
				+ ", release_date=" + release_date + "]";
	}
	
	public Long getIbn() {
		return ibn;
	}

	public void setIbn(Long ibn) {
		this.ibn = ibn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	public List<Copy> getCopies() {
		return copies;
	}

	public void setCopies(List<Copy> copies) {
		this.copies = copies;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}
}