package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
	Optional<Payment> findByPaymentId(String paymentId);
	Optional<Payment> findByOrderId(Long id);

}
