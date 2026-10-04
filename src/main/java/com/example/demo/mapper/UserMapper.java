package com.example.demo.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
@Component
public class UserMapper implements Function<User,UserDTO>{
	@Override
	public UserDTO apply(User user) {
		return new UserDTO (
				user.getId(),
				user.getEmail(),
				user.getUsername()
				);
	}
	

}
