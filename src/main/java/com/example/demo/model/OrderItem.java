package com.example.demo.model;


import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orderitem")
@Entity

public class OrderItem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;
	
	@Positive
	private double priceAtpurchase;
	
	@Min(1)
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;
	
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	

}
