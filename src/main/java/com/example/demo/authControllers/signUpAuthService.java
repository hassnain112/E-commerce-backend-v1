package com.example.demo.authControllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.authControllers.dtos.SignUpRequestDTO;
import com.example.demo.exception.DuplicateException;
import com.example.demo.model.User;
import com.example.demo.model.token;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.tokenRepository;

import jakarta.transaction.Transactional;
@Service
public class signUpAuthService {
	private UserRepository userRepo;
	private PasswordEncoder encoder;
	private tokenRepository tokenRepo;
	private EmailService emailService;
	
	public signUpAuthService(UserRepository userRepo,PasswordEncoder encoder,
			tokenRepository tokenRepo,EmailService emailService) {
		this.userRepo=userRepo;
		this.encoder=encoder;
		this.tokenRepo=tokenRepo;
		this.emailService=emailService;
	}
	@Transactional
	public User signUp(SignUpRequestDTO dto){
		if(userRepo.findByEmail(dto.email()).isPresent() || userRepo.findByUsername(dto.username()).isPresent()) {
			throw new DuplicateException("this use already exist");}
		else {
			User user = new User();
			user.setUsername(dto.username());
		    user.setEmail(dto.email());
		    user.setPassword(encoder.encode(dto.password()));
		    user.setVerfied(false);
			userRepo.save(user);
			token Token = new token();
			String token= UUID.randomUUID().toString();
			Token.setToken(token);
			Token.setTokenExp(LocalDateTime.now().plusMinutes(5));
			Token.setUser(user);
			System.out.print("Token is "+ token);
			tokenRepo.save(Token);
			
			emailService.mailSender(user.getEmail(),token);
			return user;
		}
			
			
			
			
		}
		@Transactional
		public void verifyMail(String token) {
			token T = tokenRepo.findByToken(token).orElseThrow(()-> new RuntimeException("token not found"));
			if(T.getTokenExp().isBefore(LocalDateTime.now())){
			    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token expired");
			};
			User user = T.getUser();
			user.setVerfied(true);
			tokenRepo.delete(T);
			
		}
		
	
	
}
