package com.example.demo.model;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Email(message ="invalid email format")
	@NotBlank(message = "email is required")
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotBlank(message = "password is required")
	@Size(min= 8, max = 100)
	@Column(nullable = false)
	private String password;
	
	@NotBlank(message = "username is required")
	@Column(nullable = false)
	@Size(min= 8, max = 30)
	private String username;
	
	private String role = "USER";
	
	
	private boolean isVerfied = false;
	
	@OneToMany(mappedBy ="user",cascade = CascadeType.ALL)
	private List<Order> orders = new ArrayList<>();
	
	public void addOrder(Order order) {
		this.orders.add(order);
		order.setUser(this);
	}

}
