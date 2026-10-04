//package com.example.demo.service;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.List;
//
//
//import org.springframework.stereotype.Service;
//
//import com.example.demo.authControllers.EmailService;
//import com.example.demo.enums.OrderStatus;
//import com.example.demo.model.Order;
//import com.example.demo.model.OrderItem;
//import com.example.demo.model.Product;
//import com.example.demo.repository.OrderRepository;
//import com.example.demo.repository.ProductRepository;
//import com.example.demo.repository.UserRepository;
//
//import jakarta.transaction.Transactional;
//
//@Service
//public class OrderCancellationService {
//	private final OrderRepository orderRepo;
//	private final UserRepository userRepo;
//	private final ProductRepository productRepo;
//	private final EmailService emailService;
//
//	public OrderCancellationService(OrderRepository orderRepo, UserRepository userRepo,
//	        ProductRepository productRepo, EmailService emailService) {
//	    this.orderRepo = orderRepo;
//	    this.userRepo = userRepo;
//	    this.productRepo = productRepo;
//	    this.emailService = emailService;
//	}
//	
//	@Transactional
//	public String pendingOrderFiltration(){
//		List<Order> cancellation = orderRepo.findOrderByStatus(OrderStatus.PENDING);
//		for(Order order : cancellation) {
//			LocalDateTime orderTime = order.getTime();
//			LocalDateTime currentTime = LocalDateTime.now();
//			Duration duration = Duration.between(orderTime, currentTime);
//			Boolean exceededLimit = duration.toMinutes()>30;
//			if(exceededLimit) {
//				order.setStatus(OrderStatus.CANCELLED);
//				List<OrderItem> item = order.getOrderItems();
//				for(OrderItem items : item) {
//					Product p = items.getProduct();
//					p.setStock(p.getStock()+items.getQuantity());
//					productRepo.save(p);
//				}
//				
//				String email =order.getUser().getEmail();
//				emailService.cancellationEmail(email);
//				orderRepo.save(order);
//				
//				
//			}
//			
//		}
//		return "order sweep has been performed";
//		
//	}
//
//}
