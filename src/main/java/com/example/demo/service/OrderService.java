package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderStatusDTO;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Cart;
import com.example.demo.model.CartItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;

@Service
public class OrderService {
	private final OrderRepository orderRepo;
	private final OrderMapper mapper;
	private final UserRepository userRepo;
	private final ProductRepository productRepo;
	private final CartRepository cartRepo;
	
	public OrderService(OrderRepository orderRepo,OrderMapper mapper,UserRepository userRepo,
			ProductRepository productRepo,CartRepository cartRepo) {
		this.orderRepo = orderRepo;
		this.mapper = mapper;
		this.userRepo = userRepo;
		this.productRepo = productRepo;
		this.cartRepo=cartRepo;}
	
	public List<OrderDTO> getAllOrders(){
		return orderRepo.findAll()
        .stream()
        .map(mapper)
        .collect(Collectors.toList());
	}
	@Transactional
	public OrderDTO createOrder(String username) {
		User user = userRepo.findByUsername(username).orElseThrow(()->new UserNotFoundException("user not found"));
		Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new RuntimeException("cart dose not exist"));
		List<CartItem> cartItems = cart.getItems();
		Order order = new Order();
		order.setUser(user);
		order.setStatus(OrderStatus.PENDING);
		order.setTime(LocalDateTime.now());
		Double totalPrice = 0.0;

		for(CartItem cartItem: cartItems) {
			Product product = productRepo.findById(cartItem.getProduct().getId()).orElseThrow(()->new ProductNotFoundException("product not found"));
			if(product.getStock() >= cartItem.getQuantity()) {
		OrderItem Item = new OrderItem();
		Item.setQuantity(cartItem.getQuantity());
		Item.setPriceAtpurchase(product.getPrice());
		Double orderPrice = Item.getPriceAtpurchase()*Item.getQuantity();
		totalPrice += orderPrice;
		Item.setProduct(product);
		order.addOrderItem(Item);
		product.addOrderItem(Item);
		product.setStock(product.getStock() - cartItem.getQuantity());}
			else {
			    throw new RuntimeException("Not enough stock");

			
			}

		
			
		}
		cart.getItems().clear();
		order.setOrderPrice(totalPrice);

		Order savedOrder = orderRepo.save(order);
		return mapper.apply(savedOrder);

		
	}

	public OrderDTO getById(Long id,String username , boolean isAdmin) {
		Order order = orderRepo.findById(id).orElseThrow(()-> new OrderNotFoundException("Order Not found"));
		if (!isAdmin && !order.getUser().getUsername().equals(username)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your order");
	    }
		return mapper.apply(order);	}
	public void  deleteOrder(Long id) {
		Order ord = orderRepo.findById(id).orElseThrow(()-> new OrderNotFoundException("Order Not found"));
		orderRepo.delete(ord);
		
	}
	public OrderDTO updateOrder(OrderStatusDTO dto,Long id) {
		Order ord = orderRepo.findById(id).orElseThrow(()-> new OrderNotFoundException("Order Not found"));
		
		ord.setStatus(dto.status());
		Order savedOrder = orderRepo.save(ord);
		return mapper.apply(savedOrder);		
		
	}
	

	

}
