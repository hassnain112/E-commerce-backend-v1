package com.example.demo.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
@Service
public class MyUserService implements UserDetailsService {
	private final UserRepository userRepo;
	public MyUserService(UserRepository userRepo) {
		this.userRepo=userRepo;}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUsername(username).or(() -> userRepo.findByEmail(username)).
				orElseThrow(() ->
        new UsernameNotFoundException("User not found"));
		    return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				
				.password(user.getPassword())
				.roles(user.getRole())
				.disabled(!user.isVerfied())
				.build();
				
				
				
	
	}

}
