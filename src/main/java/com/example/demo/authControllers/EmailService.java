package com.example.demo.authControllers;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	private JavaMailSender mailSender;
	public EmailService(JavaMailSender mailSender) {
		this.mailSender=mailSender;
	}
	public void mailSender(String email,String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("email verfication message");
		message.setText("http://localhost:8080/auth/verify?token=" + token);
		mailSender.send(message);
		
	}
	public void cancellationEmail(String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("your order was cancelled ");
		message.setText("your order didnt processed and was cancelled because the payment was "
				+ "stalled for too long");
		mailSender.send(message);

	}

}
