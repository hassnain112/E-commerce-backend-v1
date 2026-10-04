package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.CartDTO;
import com.example.demo.dto.CartRequestDTO;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
	private final CartRepository cartRepo;
	private final UserRepository userRepo;
	private final ProductRepository productRepo;


	public CartService(CartRepository cartRepo,UserRepository userRepo,ProductRepository productRepo) {
		this.cartRepo=cartRepo;
		this.userRepo=userRepo;
		this.productRepo=productRepo;
	}
	public List<CartDTO> getCart(String username) {
		User user =  userRepo.findByUsername(username)
				.orElseThrow(()-> new UserNotFoundException("doesnot exist"));
		Cart cart= cartRepo.findByUser(user).orElseThrow(() ->
        new RuntimeException("cart does not exist"));
		return cart.getItems().stream()
				.map(items -> new CartDTO(
				items.getProduct().getId(),
				items.getProduct().getName(),
				
				items.getProduct().getPrice(),
				items.getQuantity()))
		.toList();}
	@Transactional
	public CartDTO add(CartRequestDTO dto,String username ) {
		User user =  userRepo.findByUsername(username)
				.orElseThrow(()-> new UserNotFoundException("doesnot exist"));
		Product product = productRepo.findById(dto.productId())
	            .orElseThrow(() ->
	                new ProductNotFoundException("Product does not exist"));
		
		
		
		Optional<Cart> cart = cartRepo.findByUser(user);
		Cart cart3;
		if(cart.isEmpty()) {
			cart3 = new Cart();
			cart3.setUser(user);
		    cartRepo.save(cart3);
			
		}else {
	        cart3 = cart.get();

		}
		
		Optional <CartItem> items = cart3.getItems().stream().filter(item -> item.getProduct()
				.getId()
				.equals(product.getId())).findFirst();	
		if(items.isPresent()) {
			CartItem cart1 = items.get();
			cart1.setQuantity(cart1.getQuantity() + dto.quantity());
			
		}
		else {
			CartItem newOne = new CartItem();
			newOne.setCart(cart3);
			newOne.setProduct(product);
			newOne.setQuantity(dto.quantity());
			cart3.getItems().add(newOne);
		}
		cartRepo.save(cart3);
		
		return new CartDTO(
				product.getId(),
		        product.getName(),
		        product.getPrice(),
		        dto.quantity()
				);}}
     
