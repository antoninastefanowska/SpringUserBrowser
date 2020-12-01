package com.antonina.springapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "my_group")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<User> users;
	
	public Group() { }
	
	public Group(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public List<User> getUsers() {
		return users;
	}

	public boolean hasUser(User user) {
		return users.stream().anyMatch(x -> x.getUsername().equals(user.getUsername()));
	}

	public void addUser(User user) {
		if (users == null)
			users = new ArrayList<>();
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
}
