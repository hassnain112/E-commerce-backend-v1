package com.example.demo.service;

import com.example.demo.dto.ProductCreationDTO;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductRestockDTO;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper = new ProductMapper();

    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper)
                .collect(Collectors.toList());
    }

    
    public ProductDTO getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.apply(product);
    }
    public Product updateStock(Long id,ProductRestockDTO dto) {
    	Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    	product.setStock(product.getStock()+dto.quantity());
        return productRepository.save(product);
    }

    
    public Product createProduct(ProductCreationDTO dto) {
    	Category category = categoryRepository.findById(dto.categoryId()).orElseThrow(() -> new RuntimeException("will make catergiryNotFoundExceptionLater"));
    	Product product = new Product();
    			product.setDescription(dto.description());
    	    product.setName(dto.name());
    	    product.setPrice(dto.price());
    	    product.setStock(dto.stock());
    	    product.setCategory(category);
    	    
    	    
    	
        return productRepository.save(product);
    }

    
    public Product updateProduct(Long id, Product updatedProduct) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        
        product = updatedProduct;
        product.setId(id);

        return productRepository.save(product);
    }

    
    public void deleteProduct(Long id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }

        productRepository.deleteById(id);
    }
}