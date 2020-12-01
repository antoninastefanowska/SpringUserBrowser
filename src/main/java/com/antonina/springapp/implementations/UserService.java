package com.antonina.springapp.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.antonina.springapp.exceptions.IllegalUsernameException;
import com.antonina.springapp.exceptions.IncorrectEmailException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.exceptions.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.antonina.springapp.interfaces.IUserService;
import com.antonina.springapp.model.Group;
import com.antonina.springapp.model.User;

@Service
public class UserService implements IUserService {
	private final static Pattern EMAIL_REGEX = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
	
	@Autowired
	private RepositoryFacade repositoryFacade;
	
	@Autowired
	private HashService hashService;

	@Override
	public List<User> getUsers() {
		return repositoryFacade.findAll(User.class);
	}

	@Override
	public User createUser(String username, String password, String name, String email)
			throws IllegalUsernameException, IncorrectEmailException {

		if (repositoryFacade.findByName(username, User.class) != null)
			throw new IllegalUsernameException();
		Matcher matcher = EMAIL_REGEX.matcher(email);
		if (!matcher.matches())
			throw new IncorrectEmailException();
		
		User user = new User(username, hashService.getHash(password), name, email);
		repositoryFacade.save(user);
		return user;
	}

	@Override
	public void updateUser(String username, User newUser, List<String> groupNames)
			throws IncorrectEmailException, NoSuchUserException, NoSuchGroupException {

		User oldUser = findUser(username);

		Matcher matcher = EMAIL_REGEX.matcher(newUser.getEmail());
		if (!matcher.matches())
			throw new IncorrectEmailException();

		oldUser.setPassword(hashService.getHash(newUser.getPassword()));
		oldUser.setName(newUser.getName());
		oldUser.setEmail(newUser.getEmail());
		repositoryFacade.save(oldUser);

		List<String> oldGroupNames = new ArrayList<>();
		for (Group oldGroup : oldUser.getGroups())
			oldGroupNames.add(oldGroup.getName());

		if (groupNames != null) {
			for (String oldGroupName : oldGroupNames)
				if (!groupNames.contains(oldGroupName))
					removeUserFromGroup(oldUser.getUsername(), oldGroupName);

			for (String groupName : groupNames) {
				Group newGroup = new Group(groupName);
				if (!oldUser.inGroup(newGroup))
					addUserToGroup(oldUser.getUsername(), groupName);
			}
		} else {
			for (String oldGroupName : oldGroupNames)
				removeUserFromGroup(oldUser.getUsername(), oldGroupName);
		}
	}

	@Override
	public User findUser(String username)
			throws NoSuchUserException {
		User user = repositoryFacade.findByName(username, User.class);
		if (user == null)
			throw new NoSuchUserException();
		return user;
	}

	@Override
	public void deleteUser(String username) 
			throws NoSuchUserException {
		User user = findUser(username);
		repositoryFacade.delete(user.getId(), User.class);
	}

	@Override
	public boolean verifyUser(String username, String password)
			throws NoSuchUserException {
		User user = findUser(username);
		return user != null && user.getUsername().equals(username) && user.getPassword().equals(hashService.getHash(password));
	}

	@Override
	public void addUserToGroup(String username, String groupname) 
			throws NoSuchUserException {
		User user = findUser(username);
		if (user == null)
			throw new NoSuchUserException();
		
		Group group = repositoryFacade.findByName(groupname, Group.class);
		if (group == null) {
			group = new Group(groupname);
			repositoryFacade.save(group);
		}

		user.addGroup(group);
		group.addUser(user);
		repositoryFacade.save(user);
		repositoryFacade.save(group);
	}

	@Override
	public void removeUserFromGroup(String username, String groupname) 
			throws NoSuchUserException, NoSuchGroupException {
		
		User user = findUser(username);
		if (user == null)
			throw new NoSuchUserException();
		
		if (user.getGroups() == null)
			throw new NoSuchGroupException();
		
		Group group = null;
		for (Group userGroup : user.getGroups())
			if (userGroup.getName().equals(groupname)) {
				group = userGroup;
				break;
			}
		
		if (group == null)
			throw new NoSuchGroupException();
		
		user.removeGroup(group);
		group.removeUser(user);
		repositoryFacade.save(user);
		repositoryFacade.save(group);
	}

	@Override
	public List<User> findUsersByGroup(String groupname) 
			throws NoSuchGroupException {

		Group group = repositoryFacade.findByName(groupname, Group.class);
		if (group == null)
			throw new NoSuchGroupException();
		
		return group.getUsers();
	}
}
