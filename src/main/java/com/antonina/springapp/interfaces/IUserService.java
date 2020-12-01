package com.antonina.springapp.interfaces;

import java.util.List;

import com.antonina.springapp.exceptions.IllegalUsernameException;
import com.antonina.springapp.exceptions.IncorrectEmailException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.exceptions.NoSuchUserException;
import com.antonina.springapp.model.Group;
import com.antonina.springapp.model.User;

public interface IUserService {
	List<User> getUsers();
	User createUser(String username, String password, String name, String email) 
			throws IllegalUsernameException, IncorrectEmailException;
	void updateUser(String username, User newUser, List<String> groupNames)
			throws IncorrectEmailException, NoSuchUserException, NoSuchGroupException;
	User findUser(String username)
			throws NoSuchUserException;
	void deleteUser(String username) 
			throws NoSuchUserException;
	boolean verifyUser(String username, String password)
			throws NoSuchUserException;
	void addUserToGroup(String username, String groupname)
			throws NoSuchUserException;
	void removeUserFromGroup(String username, String groupname)
			throws NoSuchUserException, NoSuchGroupException;
	List<User> findUsersByGroup(String groupname)
			throws NoSuchGroupException;
}
