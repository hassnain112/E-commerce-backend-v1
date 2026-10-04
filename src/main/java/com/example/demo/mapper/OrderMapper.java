package com.example.demo.mapper;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.model.Order;
@Component
public class OrderMapper implements Function<Order,OrderDTO>{
	
	@Override
	public OrderDTO apply(Order order) {
		List<OrderItemDTO> items  = order.getOrderItems().stream()
				.map(item -> new  OrderItemDTO(
						item.getProduct().getId(),
						item.getProduct().getName(),
						item.getProduct().getPrice(),
						item.getQuantity()
						)).toList();
		
		
		return new OrderDTO(
				order.getId(),
				order.getTime(),
				order.getOrderPrice(),
				items			);
	}

}
