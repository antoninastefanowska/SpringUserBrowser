package com.antonina.springapp.interfaces;

import com.antonina.springapp.exceptions.IllegalGroupnameException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.model.Group;

import java.util.List;

public interface IGroupService {
    List<Group> getGroups();
    Group findGroup(String groupname)
            throws NoSuchGroupException;
    void createGroup(String groupname)
            throws IllegalGroupnameException;
    void deleteGroup(String groupname)
            throws NoSuchGroupException;
}
