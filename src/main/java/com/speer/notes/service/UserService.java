package com.speer.notes.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.speer.notes.entity.User;
import com.speer.notes.model.UserDto;
import com.speer.notes.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public void registerNewUser(UserDto userDto) {
		/*
		 * if (userRepository.findByUsername(userDto.getUsername()) != null) { throw new
		 * RuntimeException("Username already exists"); }
		 */
		User newUser = new User();
		newUser.setUsername(userDto.getUsername());
		newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
		userRepository.save(newUser);
	}

	public Optional<User> authenticateUser(String username, String password) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (passwordEncoder.matches(password, user.getPassword())) {
				return userOptional;
			}
		}
		return Optional.empty();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.get().getUsername(),
				user.get().getPassword(), new ArrayList<>());
	}

}
