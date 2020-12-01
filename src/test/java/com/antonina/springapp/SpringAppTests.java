package com.antonina.springapp;

import java.util.List;

import com.antonina.springapp.exceptions.IllegalUsernameException;
import com.antonina.springapp.exceptions.IncorrectEmailException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.exceptions.NoSuchUserException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.antonina.springapp.implementations.UserService;
import com.antonina.springapp.model.User;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SpringAppTests {
	@Autowired
	private UserService userService;
	
	@Test
	public void createValidUser() {
		try {
			User user = userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			assert user != null;
		} catch (IllegalUsernameException | IncorrectEmailException e) {
			assert false;
		}
	}
	
	@Test
	public void createInvalidEmailUser() {
		try {
			userService.createUser("anna", "pass", "Anna", "aaa");
			assert false;
		} catch (IncorrectEmailException e) {
			assert true;
		} catch (IllegalUsernameException e) {
			assert false;
		}
	}
	
	@Test
	public void createExistingUser() {
		try {
			userService.createUser("jankowalski", "pass", "Jan", "jan@o2.pl");
			userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			assert false;
		} catch (IllegalUsernameException e) {
			assert true;
		} catch (IncorrectEmailException e) {
			assert false;
		}
	}
	
	@Test
	public void findExistingUser() {
		try {
			userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			assert userService.findUser("jankowalski") != null;
		} catch (IllegalUsernameException | IncorrectEmailException | NoSuchUserException e) {
			assert false;
		}
	}
	
	@Test
	public void findNonExistingUser() {
		try {
			userService.findUser("karol12");
			assert false;
		} catch (NoSuchUserException e) {
			assert true;
		}
	}
	
	@Test
	public void verifyExistingUser() {
		try {
			userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			assert userService.verifyUser("jankowalski", "haslo");
		} catch (IllegalUsernameException | IncorrectEmailException | NoSuchUserException e) {
			assert false;
		}
	}
	
	@Test
	public void deleteExistingUser() {
		try {
			userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			userService.deleteUser("jankowalski");
			assert true;
		} catch (IllegalUsernameException | IncorrectEmailException | NoSuchUserException e) {
			assert false;
		}
	}
	
	@Test
	public void deleteNonExistingUser() {
		try {
			userService.deleteUser("karol12");
			assert false;
		} catch (NoSuchUserException e) {
			assert true;
		}
	}
	
	@Test
	public void verifyNonExistingUser() {
		try {
			userService.verifyUser("anna", "pass");
			assert false;
		} catch (NoSuchUserException e) {
			assert true;
		}
	}
	
	@Test
	public void addUserToGroup() {
		try {
			userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			userService.addUserToGroup("jankowalski", "group1");
			List<User> users = userService.findUsersByGroup("group1");
			boolean found = false;
			for (User user : users)
				if (user.getUsername().equals("jankowalski")) {
					found = true;
					break;
				}
			assert found;
		} catch (IllegalUsernameException | IncorrectEmailException | NoSuchUserException | NoSuchGroupException e) {
			assert false;
		}
	}
	
	@Test
	public void removeUserFromExistingGroup() {
		try {
			userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			userService.addUserToGroup("jankowalski", "group1");
			userService.removeUserFromGroup("jankowalski", "group1");
			assert true;
		} catch (IllegalUsernameException | IncorrectEmailException | NoSuchUserException | NoSuchGroupException e) {
			assert false;
		}
	}
	
	@Test
	public void removeUserFromNonExistingGroup() {
		try {
			userService.createUser("jankowalski", "haslo", "Jan", "janko@gmail.com");
			userService.removeUserFromGroup("jankowalski", "group2");
			assert false;
		} catch (NoSuchGroupException e) {
			assert true;
		} catch (IllegalUsernameException | IncorrectEmailException | NoSuchUserException e) {
			assert false;
		}
	} 
}
