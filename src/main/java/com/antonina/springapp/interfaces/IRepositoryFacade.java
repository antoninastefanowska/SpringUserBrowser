package com.antonina.springapp.interfaces;

import java.util.List;

public interface IRepositoryFacade {
	<T> void save(T obj);
	<T> List<T> findAll(Class<T> clazz);
	<T> T findById(Long id, Class<T> clazz);
	<T> T findByName(String name, Class<T> clazz);
	boolean delete(Long id, Class clazz);
}
