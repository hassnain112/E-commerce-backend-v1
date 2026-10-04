package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;




public record OrderDTO(Long id,LocalDateTime time,Double orderPrice ,List<OrderItemDTO> items) {

}
