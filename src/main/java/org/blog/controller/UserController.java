package org.blog.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.blog.model.User;
import org.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/blog/")
public class UserController {
	
	@Autowired
	private UserService userService;
 	
	@PostMapping("/signup")
	public ResponseEntity<Map<String,String>> singup(@RequestBody User user)
	{
		this.userService.addUser(user);
		Map<String,String> response=new HashMap<String,String>();
		response.put("status", "success");
		response.put("message", "User registered!!");
		
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/login")
	public ResponseEntity<Map<String,String>> login(@RequestParam("email") String email,@RequestParam("password") String password)
	{
		Optional<User> existingUser=this.userService.getUserByEmail(email);
		Map<String,String> response=new HashMap<String,String>();
		if(existingUser.isPresent())
		{
			if(existingUser.get().getUserPassword().equals(password))
			{
				
				response.put("status", "success");
				response.put("message", "User authenticated");
				response.put("userId",String.valueOf( existingUser.get().getUserId()) );
				response.put("userRole", existingUser.get().getRole());
				return new ResponseEntity<Map<String,String>>(response,HttpStatus.OK);
			}
			else
			{
				response.put("status", "Failed");
				response.put("message", "User password inncorrect");
				return new ResponseEntity<Map<String,String>>(response,HttpStatus.NOT_FOUND);
			}
		}		
		else
		{
			response.put("status", "Failed");
			response.put("message", "User email does not exist");
			return new ResponseEntity<Map<String,String>>(response,HttpStatus.NOT_FOUND);
		}
	}
	
}
