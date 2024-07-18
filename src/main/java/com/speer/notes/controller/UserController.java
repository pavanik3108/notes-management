package com.speer.notes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.speer.notes.entity.User;
import com.speer.notes.exception.UserAlreadyExistsException;
import com.speer.notes.model.AuthTokenResponse;
import com.speer.notes.model.UserDto;
import com.speer.notes.service.UserService;
import com.speer.notes.utils.JwtTokenProviderUtils;

@RestController
@RequestMapping("/auth")
public class UserController {

	private static final String REG_SUCCESS_MSG = "User registered successfully.";

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenProviderUtils jwtTokenProviderUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(@RequestBody UserDto user) {
		try {
			userService.registerNewUser(user);
			return ResponseEntity.status(HttpStatus.CREATED).body(REG_SUCCESS_MSG);
		} catch (UserAlreadyExistsException ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody User user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwtToken = jwtTokenProviderUtils.generateToken(user.getUsername());
		return ResponseEntity.ok(new AuthTokenResponse(jwtToken));
	}

}
