package fr.pierre.batch.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "roles")
public class Role implements Serializable{

	public Role() {
		
	}
	
	public Role(Long id, String name, List<User> user) {
		this.id = id;
		this.name = name;
		this.users = user;
	}
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	@Column(nullable = false, unique = true)
    private String name;
    
	//---------------------------------------------------------------------------------
	
	@ManyToMany(mappedBy = "roles")
    private List <User> users;

	//---------------------------------------------------------------------------------

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
	
	public Long getId() {
		return id;
	}

	public void setId(long l) {
		this.id = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
