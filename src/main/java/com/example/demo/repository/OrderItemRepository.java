package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	@Query("SELECT p FROM Order o JOIN o.orderItems oi JOIN oi.product p WHERE o.id = :id")
	List<Product> productsInAnOrder(@Param("id")Long id);

}
