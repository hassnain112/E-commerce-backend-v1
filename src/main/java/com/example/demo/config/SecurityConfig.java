package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.List;

import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.example.demo.service.MyUserService;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
public class SecurityConfig {
	private final MyUserService myUserService;

    public SecurityConfig(MyUserService myUserService) {
        this.myUserService = myUserService;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    
	@SuppressWarnings("deprecation")
	@Bean
    public AuthenticationProvider authenticationProvider() {
    	  DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    	  
		  provider.setUserDetailsService(myUserService);
		  provider.setPasswordEncoder(passwordEncoder());
		  return provider;}
	
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
		        .cors(cors -> {})
		        .csrf(csrf -> csrf
		        	    .ignoringRequestMatchers("/stripe/webhook")
		        	)

		        .httpBasic(Customizer.withDefaults())
		        
		        .formLogin(form -> form
		        	    .loginProcessingUrl("/login")
		        	    .successHandler((request, response, authentication) -> {
		        	        response.setStatus(HttpServletResponse.SC_OK);
		        	    })
		        	    .failureHandler((request, response, exception) -> {
		        	        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		        	    })
		        	    .permitAll()
		        	)
		        	
		        .authorizeHttpRequests(auth -> auth
		        	    .requestMatchers("/auth/**", "/login", "/csrf", "/stripe/webhook", "/test/**").permitAll()
		        	    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		        	    .requestMatchers(HttpMethod.GET, "/product/**").permitAll()
		        	    .requestMatchers("/product/**","/category").hasRole("ADMIN")
		        	    .requestMatchers("/user", "/user/**").hasRole("ADMIN")
		        	    .requestMatchers("/order/getall", "/order/update/**", "/order/delete/**").hasRole("ADMIN")
		        	    .anyRequest().authenticated())
.build();
		}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

	    CorsConfiguration configuration = new CorsConfiguration();

	    configuration.setAllowedOrigins(
       List.of("http://localhost:"));  //add your own frontend port
	

	    configuration.setAllowedMethods(
	        List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
	    );

	    configuration.setAllowedHeaders(
	        List.of("*")
	    );

	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source =
	        new UrlBasedCorsConfigurationSource();

	    source.registerCorsConfiguration("/**", configuration);

	    return source;
	}
		  
    	  
    	  
    	  
	
			
       
            
          

        
        
    }
