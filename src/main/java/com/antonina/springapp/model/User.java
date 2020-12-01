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

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	private String name;
	
	private String email;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Group> groups;
	
	public User() { }
	
	public User(String username, String password, String name, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public List<Group> getGroups() {
		return groups;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean inGroup(Group group) {
		return groups.stream().anyMatch(x -> x.getName().equals(group.getName()));
	}

	public void addGroup(Group group) {
		if (groups == null)
			groups = new ArrayList<>();
		groups.add(group);
	}


	public void removeGroup(Group group) {
		groups.remove(group);
	}
	
	@Override
	public String toString() {
		return username + " " + password + " " + name + " " + email;
	}
}
