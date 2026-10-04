package com.example.demo.dto;

import jakarta.validation.constraints.Positive;

public record ProductRestockDTO(@Positive(message = "Quantity must be greater than 0")int quantity) {

}
