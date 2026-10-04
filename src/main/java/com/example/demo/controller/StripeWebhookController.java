package com.example.demo.controller;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.enums.OrderStatus;
import com.example.demo.repository.OrderRepository;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {
    
    private final OrderRepository orderRepo;

    public StripeWebhookController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @Value("${stripe.webhook.secret}")
    private String secretKey;

    @PostMapping("/webhook")
    public ResponseEntity<String> handlePayResponseFromStripe(
            @RequestBody byte[] rawPayload,
            @RequestHeader("stripe-signature") String signatureHeader) throws EventDataObjectDeserializationException {
        
        String payload = new String(rawPayload, StandardCharsets.UTF_8);
        
        try {
            Event event = Webhook.constructEvent(payload, signatureHeader, secretKey);
            
            if ("payment_intent.succeeded".equals(event.getType())) {
                System.out.println("PAYMENT SUCCESSFUL");
                EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
                if(deserializer.getObject().isPresent()) {
                
                PaymentIntent paymentIntent = (PaymentIntent) deserializer.getObject().get();
                
                if (paymentIntent == null) {
                    System.out.println("Could not deserialize PaymentIntent");
                    return ResponseEntity.badRequest().body("Could not deserialize PaymentIntent");
                }
                
                Map<String, String> metaData = paymentIntent.getMetadata();
                String orderId1 = metaData.get("order_id");
                

                
                if (orderId1 != null) {
                
                    Long orderId = Long.parseLong(orderId1);
                    
                    // Update your database entity
                    orderRepo.findById(orderId).ifPresent(order -> {
                        order.setStatus(OrderStatus.PAID);
                        orderRepo.save(order);
                        System.out.println("Order " + orderId + " successfully marked as PAID in DB.");
                    });
                	}
            }}
                
            else if("payment_intent.payment_failed".equals(event.getType()) ) {
                EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
                if(deserializer.getObject().isPresent()) {

                PaymentIntent paymentIntent = (PaymentIntent) deserializer.getObject().get();
                if(paymentIntent != null) {
                Map<String, String> metaData = paymentIntent.getMetadata();
                String orderId1 = metaData.get("order_id");


            	   if(orderId1 != null && !orderId1.isBlank()) {

                       orderRepo.findById(Long.parseLong(orderId1)).ifPresent(order -> {
                    	   order.setStatus(OrderStatus.PENDING);
                    	   orderRepo.save(order);
});
                    	   
                       

            	   }
            	   }
            	
                }}
            return ResponseEntity.ok("received");

        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("declined");
        }
    }
}
