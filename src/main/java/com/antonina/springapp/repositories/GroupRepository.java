package com.antonina.springapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.antonina.springapp.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {	
	@Query("SELECT g FROM Group g WHERE g.name = :name")
	Group findByName(String name);
}
