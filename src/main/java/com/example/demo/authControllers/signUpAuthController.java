package com.example.demo.authControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.authControllers.dtos.SignUpRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class signUpAuthController {
	private signUpAuthService authService;
	public signUpAuthController(signUpAuthService authService) {
		this.authService=authService;  }
	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDTO dto){
		authService.signUp(dto); 
	    return ResponseEntity.status(HttpStatus.CREATED).body("Signup successful, check your email to verify");
}
	@GetMapping("/verify")
	public ResponseEntity<String> verify(@RequestParam String token){
		authService.verifyMail(token);
		return ResponseEntity.ok("Email verified Successfully");
		
	}
	

}
