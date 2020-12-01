package com.antonina.springapp.implementations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.antonina.springapp.interfaces.IRepositoryFacade;
import com.antonina.springapp.model.Group;
import com.antonina.springapp.model.User;
import com.antonina.springapp.repositories.GroupRepository;
import com.antonina.springapp.repositories.UserRepository;

@Repository
public class RepositoryFacade implements IRepositoryFacade {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Override
	public <T> void save(T obj) {
		if (obj.getClass() == User.class) {
			User user = (User)obj;
			user = userRepository.save(user);
		} else if (obj.getClass() == Group.class) {
			Group group = (Group)obj;
			group = groupRepository.save(group);
		}
	}

	@Override
	public <T> List<T> findAll(Class<T> clazz) {
		if (clazz == User.class)
			return (List<T>)userRepository.findAll();
		else if (clazz == Group.class)
			return (List<T>)groupRepository.findAll();
		else
			return null;
	}

	@Override
	public <T> T findById(Long id, Class<T> clazz) {
		if (clazz == User.class)
			return (T)userRepository.findById(id);
		else if (clazz == Group.class)
			return (T)groupRepository.findById(id);
		else
			return null;
	}

	@Override
	public <T> T findByName(String name, Class<T> clazz) {
		if (clazz == User.class)
			return (T)userRepository.findByUsername(name);
		else if (clazz == Group.class)
			return(T)groupRepository.findByName(name);
		else
			return null;
	}

	@Override
	public boolean delete(Long id, Class clazz) {
		if (clazz == User.class && userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}
		else if (clazz == Group.class && groupRepository.existsById(id)) {
			groupRepository.deleteById(id);
			return true;
		} else
			return false;
	}
}
