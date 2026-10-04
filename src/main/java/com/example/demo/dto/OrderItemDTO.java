package com.example.demo.dto;
//this is used to display the items in the receipt as i am using a cart based system now!!
public record OrderItemDTO(Long prductID,String productName, Double priceAtPurchase , int quantity) {

}
