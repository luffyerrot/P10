package fr.pierre.apirest.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User implements Serializable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@Column(nullable=false)
    private String password;
	
	@Column(nullable=false)
	private String username;
	
	public User() {
		
	}

	public User(Long id, String email, String username) {
		this.id = id;
		this.email = email;
		this.username = username;
	}
	
	//---------------------------------------------------------------------------------
	   
	@ManyToMany(cascade=CascadeType.MERGE)
    @JoinTable(name="user_role", joinColumns={@JoinColumn(name="user_id", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="role_id", referencedColumnName="id")})
    private List <Role> roles;
	
	@OneToMany(mappedBy = "user")
    private List <Booking> bookings;
	
	@OneToMany(mappedBy = "user")
    private List <Request> requests;
    
    //----------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", username=" + username + "]";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}
}