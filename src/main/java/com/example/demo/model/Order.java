package com.example.demo.model;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.enums.OrderStatus;

import jakarta.persistence.*;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
@Entity


public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	private double orderPrice;
	

	
	private LocalDateTime time;
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.PENDING;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
	
	@OneToMany(mappedBy="order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private List<OrderItem> orderItems= new ArrayList<>();
	public void addOrderItem(OrderItem item) {
		item.setOrder(this);
		orderItems.add(item);
	}
	@OneToOne(mappedBy ="order",cascade = CascadeType.ALL)
	private Payment payemnt;
	
	
}
