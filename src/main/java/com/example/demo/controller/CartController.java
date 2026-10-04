package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartRequestDTO;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
	private final CartService cartService;
	public CartController(CartService cartService) {
		this.cartService=cartService;
		
	}
	@PostMapping("/add")
	public ResponseEntity<CartDTO> add(@RequestBody CartRequestDTO dto,
			@AuthenticationPrincipal UserDetails authentiaction){
		String username = authentiaction.getUsername();
		return ResponseEntity.ok(cartService.add(dto,username));
		
	}
	@GetMapping("/get")
	public ResponseEntity<List<CartDTO>> get(@AuthenticationPrincipal UserDetails user){
		String username = user.getUsername();
		return ResponseEntity.ok(cartService.getCart(username));
		
	}
	

}
