package com.example.demo.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
@Service
public class UserService {
	private final UserRepository userRepo;
	private final UserMapper mapper;
	private final PasswordEncoder encoder;
	
	public UserService(UserRepository userRepo,UserMapper mapper,PasswordEncoder encoder) {
		this.userRepo=userRepo;
		this.mapper=mapper;
		this.encoder=encoder;
	}
	
    public List<UserDTO> getAllUsers(){
    	    return userRepo.findAll().stream().map(mapper).collect(Collectors.toList());
    	
    }
    public UserDTO getUser(Long id){
    	   User user = userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

    	    return mapper.apply(user);
    	    	}
    public User createUser(User user) {
    	   String EncodePass = encoder.encode(user.getPassword());
    	   user.setPassword(EncodePass);
    	   return userRepo.save(user);
    	
    }
    public User updateUser(Long id, User updatedUser) {

        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        user = updatedUser;
        user.setId(id);

        return userRepo.save(user);
    }

    public void deleteUser(Long id) {

        if (!userRepo.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }

        userRepo.deleteById(id);
    }
}
