package com.antonina.springapp;

import java.util.List;
import java.util.Scanner;

import com.antonina.springapp.exceptions.IllegalUsernameException;
import com.antonina.springapp.exceptions.IncorrectEmailException;
import com.antonina.springapp.exceptions.NoSuchGroupException;
import com.antonina.springapp.exceptions.NoSuchUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.antonina.springapp.implementations.UserService;
import com.antonina.springapp.model.User;

@Component
@Profile("!test")
public class ConsoleApp implements CommandLineRunner {

	@Autowired
	private UserService userService;
	
	@Override
	public void run(String... args) {
		Scanner input = new Scanner(System.in);
		while (true) {
			String command = input.nextLine();
			String[] words = command.split(" ");
			
			switch (words[0]) {
				case "create": {
					try {
						User user = userService.createUser(words[1], words[2], words[3], words[4]);
						System.out.println(user.toString());
					} catch (IllegalUsernameException | IncorrectEmailException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
				case "find": {
					try {
						User user = userService.findUser(words[1]);
						System.out.println(user.toString());
					} catch (NoSuchUserException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
				case "delete": {
					try {
						userService.deleteUser(words[1]);
						System.out.println("Usunięto użytkownika.");
					} catch (NoSuchUserException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
				case "login": {
					try {
						if (userService.verifyUser(words[1], words[2]))
							System.out.println("Zalogowano.");
						else
							System.out.println("Niepoprawne dane.");
					} catch (NoSuchUserException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
				case "add-to-group": {
					try {
						userService.addUserToGroup(words[1], words[2]);
						System.out.println("Dodano użytkownika do grupy.");
					} catch (NoSuchUserException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
				case "remove-from-group": {
					try {
						userService.removeUserFromGroup(words[1], words[2]);
						System.out.println("Usunięto użytkownika z grupy.");
					} catch (NoSuchUserException | NoSuchGroupException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
				case "find-users-by-group": {
					try {
						List<User> users = userService.findUsersByGroup(words[1]);
						for (User user : users)
							System.out.println(user.toString());
					} catch (NoSuchGroupException e) {
						System.out.println(e.getMessage());
					}
					break;
				}
			}
		}
	}
}
