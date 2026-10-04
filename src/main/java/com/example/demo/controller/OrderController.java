package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderStatusDTO;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
@RequestMapping("/order")
@RestController
public class OrderController {
	private final OrderService orderService;
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	@GetMapping("/getall")
	public List<OrderDTO> getAllOrders(){
		return orderService.getAllOrders();
	}
	@PostMapping("/create")
	public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal UserDetails user){
		String username = user.getUsername();
		return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(username));
	}
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<OrderDTO> getById(@PathVariable Long id,@AuthenticationPrincipal UserDetails user){
		boolean isAdmin = user.getAuthorities().stream()
		        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		return ResponseEntity.ok(orderService.getById(id,user.getUsername(),isAdmin));
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
		orderService.deleteOrder(id);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		
	}
	@PutMapping("/update/{id}")
	public ResponseEntity<OrderDTO>	updateOrder(@PathVariable Long id ,@RequestBody OrderStatusDTO dto){
		return ResponseEntity.ok(orderService.updateOrder(dto,id));

		
	}
	

}
