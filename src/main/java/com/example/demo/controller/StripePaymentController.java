package com.example.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.StripeResponseDTO;
import com.example.demo.service.PaymentService;
import com.stripe.exception.StripeException;


@RestController
@RequestMapping("/stripe/payment")
public class StripePaymentController {
	private final PaymentService payService;
	public StripePaymentController(PaymentService payService) {
		this.payService=payService;
	}
	@PostMapping("/{orderId}")
	public ResponseEntity<StripeResponseDTO> payment(@PathVariable Long orderId,@AuthenticationPrincipal UserDetails user) throws StripeException{
		String username  = user.getUsername();

		return payService.processPay(orderId,username);
	}
	
	

}
