package com.example.demo.repository;

import com.example.demo.model.Product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT p FROM Product p WHERE p.stock < :stock")
	List<Product> StockLessThanTen(@Param("stock")int stock);
	
	@Query("SELECT p FROM Product p WHERE p.name LIKE %:name%")
	List<Product> productContaining(@Param("name")String name);
	
	@Query("SELECT p FROM Product p WHERE p.stock > :stock AND p.price >:price ORDER BY p.price DESC,p.name ASC")
	List<Product> custome(@Param("stock")int stock, @Param("price") double price);
	
	@Query("SELECT p FROM Product p JOIN  p.category c WHERE c.name = :name")
	List<Product> getProductsOfACategory(@Param("name")String name);
	@Query("""
		    SELECT p
		    FROM Product p
		    JOIN p.category c
		    WHERE c.name = :category
		    AND p.price BETWEEN :minPrice AND :maxPrice
		    AND p.stock > :stock
		    ORDER BY p.price ASC
		""")
		Page<Product> searchProducts(
		        @Param("category") String category,
		        @Param("minPrice") double minPrice,
		        @Param("maxPrice") double maxPrice,
		        @Param("stock") int stock,
		        Pageable pageable
		);
	
	




}