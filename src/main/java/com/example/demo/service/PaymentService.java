package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dto.StripeResponseDTO;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exception.OrderNotFoundException;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
@Service
public class PaymentService {
	@SuppressWarnings("unused")
	private final PaymentRepository paymentRepo;
	private final OrderRepository orderRepo;
	public PaymentService(PaymentRepository paymentRepo,OrderRepository orderRepo) {
		this.paymentRepo=paymentRepo;
		this.orderRepo=orderRepo;
		}
	public ResponseEntity<StripeResponseDTO> processPay(Long orderId,String username) throws StripeException{
		Order order = orderRepo.findById(orderId).orElseThrow(()-> new OrderNotFoundException("Order does not exist"));
		if(order.getStatus() == OrderStatus.PAID) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"This order has already been paid for");
					
		}
		
		if(!order.getUser().getUsername().equals(username)){
			throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Process cant go any further as authrization failed");
			
		}
		else {
			List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
			for(OrderItem item : order.getOrderItems()) {
			SessionCreateParams.LineItem lineitem = 
							SessionCreateParams.LineItem.builder()
							.setQuantity((long)item.getQuantity())
							.setPriceData(
									SessionCreateParams.LineItem.PriceData.builder()
									.setCurrency("usd")
									.setUnitAmount(Math.round(item.getPriceAtpurchase()*100))
									.setProductData(
											SessionCreateParams.LineItem.PriceData.ProductData.builder()
											.setName(item.getProduct().getName())
											.build()
											)
									.build()
									)
							.build();
			lineItems.add(lineitem);}
			SessionCreateParams.PaymentIntentData paymentIntentData = 
			        SessionCreateParams.PaymentIntentData.builder()
			                .putMetadata("order_id", String.valueOf(order.getId())) // Pass the dynamic order ID
			                .putMetadata("user_email", order.getUser().getEmail()) // Pass the actual dynamic email string
			                .build();
					SessionCreateParams parms = SessionCreateParams.builder()
							.setMode(SessionCreateParams.Mode.PAYMENT)
							.setSuccessUrl("http://localhost:8080/test/success")
							.setCancelUrl("http://localhost:8080/test/failure")
							.addAllLineItem(lineItems)
							.setPaymentIntentData(paymentIntentData)
							.build();
					
					
							
					Session session = Session.create(parms);
					return ResponseEntity.ok(new StripeResponseDTO(session.getUrl()));

					
					
					
					
		
		}
		
		
			
		
		
		
	}
}
