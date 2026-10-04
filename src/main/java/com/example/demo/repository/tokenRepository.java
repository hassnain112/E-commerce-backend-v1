package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.token;

public interface tokenRepository extends JpaRepository<token,Long>{
	Optional<token> findByToken(String token);

}
