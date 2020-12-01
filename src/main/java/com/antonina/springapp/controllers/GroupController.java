package com.antonina.springapp.controllers;

import com.antonina.springapp.exceptions.IllegalGroupnameException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.exceptions.NoSuchUserException;
import com.antonina.springapp.implementations.GroupService;
import com.antonina.springapp.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/groups")
    public String getGroups(Model model) {
        model.addAttribute("groups", groupService.getGroups());
        return "groups";
    }

    @GetMapping("/groups/{groupname}")
    public String getGroup(@PathVariable("groupname") String groupname, Model model)
            throws NoSuchGroupException {
        Group group = groupService.findGroup(groupname);
        model.addAttribute("group", group);
        return "group";
    }

    @GetMapping("/groups/create")
    public String createGroup(Model model) {
        model.addAttribute("group", new Group());
        return "group-create";
    }

    @PostMapping("/groups/create")
    public String createGroup(@Valid Group group, BindingResult result, Model model) {
        if (result.hasErrors())
            return "group-create";

        try {
            groupService.createGroup(group.getName());
        } catch (IllegalGroupnameException e) {
            result.rejectValue("name", "error.group", e.getMessage());
        }

        if (result.hasErrors())
            return "group-create";

        model.addAttribute("groups", groupService.getGroups());
        return "redirect:/groups";
    }

    @GetMapping("/groups/delete/{name}")
    public String deleteGroup(@PathVariable("name") String name, Model model)
            throws NoSuchGroupException {
        groupService.deleteGroup(name);
        model.addAttribute("groups", groupService.getGroups());
        return "redirect:/groups";
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView databaseException() {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Przed usunięciem usuń powiązania użytkowników z grupami.");
        return mav;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchGroupException.class)
    public ModelAndView notFoundException(Exception exception) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", exception.getMessage());
        return mav;
    }
}
