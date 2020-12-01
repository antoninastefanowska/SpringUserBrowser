package com.antonina.springapp.implementations;

import com.antonina.springapp.exceptions.IllegalGroupnameException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.interfaces.IGroupService;
import com.antonina.springapp.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService implements IGroupService {

    @Autowired
    private RepositoryFacade repositoryFacade;

    @Override
    public List<Group> getGroups() {
        return repositoryFacade.findAll(Group.class);
    }

    @Override
    public Group findGroup(String groupname)
            throws NoSuchGroupException {
        Group group = repositoryFacade.findByName(groupname, Group.class);
        if (group == null)
            throw new NoSuchGroupException();
        return group;
    }

    @Override
    public void createGroup(String groupname)
            throws IllegalGroupnameException {
        if (repositoryFacade.findByName(groupname, Group.class) != null)
            throw new IllegalGroupnameException();
        Group group = new Group(groupname);
        repositoryFacade.save(group);
    }

    @Override
    public void deleteGroup(String groupname)
            throws NoSuchGroupException {
        Group group = findGroup(groupname);
        repositoryFacade.delete(group.getId(), Group.class);
    }
}
