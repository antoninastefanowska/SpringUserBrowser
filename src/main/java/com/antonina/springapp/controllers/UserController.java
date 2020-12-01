package com.antonina.springapp.controllers;

import com.antonina.springapp.exceptions.IllegalUsernameException;
import com.antonina.springapp.exceptions.IncorrectEmailException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.exceptions.NoSuchUserException;
import com.antonina.springapp.implementations.GroupService;
import com.antonina.springapp.implementations.UserService;
import com.antonina.springapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users";
    }

    @GetMapping("/users/{username}")
    public String getUser(@PathVariable("username") String username, Model model)
            throws NoSuchUserException {
        User user = userService.findUser(username);
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/users/create")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "user-create";
    }

    @PostMapping("/users/create")
    public String createUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors())
            return "user-create";
        try {
            userService.createUser(user.getUsername(), user.getPassword(), user.getName(), user.getEmail());
        } catch (IllegalUsernameException e) {
            result.rejectValue("username", "error.user", e.getMessage());
        } catch (IncorrectEmailException e) {
            result.rejectValue("email", "error.user", e.getMessage());
        }
        if (result.hasErrors())
            return "user-create";

        model.addAttribute("users", userService.getUsers());
        return "redirect:/users";
    }

    @GetMapping("/users/update/{username}")
    public String updateUser(@PathVariable("username") String username, Model model)
            throws NoSuchUserException {
        User user = userService.findUser(username);
        model.addAttribute("user", user);
        model.addAttribute("groups", groupService.getGroups());
        return "user-update";
    }

    @PostMapping("/users/update/{username}")
    public String updateUser(@PathVariable("username") String username, @RequestParam(name = "group-names", required = false) List<String> groupNames, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors())
            return "user-update";
        try {
            userService.updateUser(username, user, groupNames);
        } catch (NoSuchUserException e) {
            result.rejectValue("username", "error.user", e.getMessage());
        } catch (NoSuchGroupException e) {
            result.rejectValue("groups", "error.groups", e.getMessage());
        } catch (IncorrectEmailException e) {
            result.rejectValue("email", "error.user", e.getMessage());
        }
        if (result.hasErrors())
            return "user-update";
        model.addAttribute("users", userService.getUsers());
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{username}")
    public String deleteUser(@PathVariable("username") String username, Model model)
            throws NoSuchUserException {
        userService.deleteUser(username);
        model.addAttribute("users", userService.getUsers());
        return "redirect:/users";
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView databaseException() {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Przed usunięciem usuń powiązania użytkowników z grupami.");
        return mav;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchUserException.class)
    public ModelAndView notFoundException(Exception exception) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", exception.getMessage());
        return mav;
    }
}
