package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class StripeTestingConrollers {
	@GetMapping("/success")
	public String success() {
		return "<h1> the payment was succesful";
	}
	@GetMapping("/failure")
	public String failure() {
		return "<h1> the payment was failure";
	}

}
