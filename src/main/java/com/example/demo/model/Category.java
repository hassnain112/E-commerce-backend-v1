package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "category")
@Entity


public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private long id;
	
	@NotBlank(message ="Must not be null")
	@Column(nullable = false, unique = true)
	private String name;
	
	@OneToMany(mappedBy ="category")
	private List<Product> products = new ArrayList<>();

}
