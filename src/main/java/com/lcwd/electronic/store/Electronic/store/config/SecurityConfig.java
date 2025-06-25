package com.lcwd.electronic.store.Electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    //security filter bean
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        //urls
        //public
        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
        //protected
        //admin
        //normal
        security.authorizeHttpRequests(request ->
         request.requestMatchers(HttpMethod.POST,"/user").permitAll()
                 .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.DELETE, "/user").hasAnyRole("ADMIN", "USER")
                 .requestMatchers(HttpMethod.PUT, "/user").hasAnyRole("ADMIN", "USER")
                 .requestMatchers(HttpMethod.POST, "/products").hasAnyRole("ADMIN")
                 .requestMatchers(HttpMethod.GET, "/products").permitAll()
                 .requestMatchers(HttpMethod.PUT, "/products").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.DELETE, "/products").hasRole("ADMIN")
                 .requestMatchers("/carts").permitAll()
                 .requestMatchers(HttpMethod.GET, "/categories").permitAll()
                 .requestMatchers("/categories").hasRole("ADMIN")
                 .requestMatchers(HttpMethod.PUT, "/orders").hasRole("ADMIN")
                 .requestMatchers("/orders").hasAnyRole("USER", "ADMIN")
                 .anyRequest().authenticated()

        );


        security.httpBasic(Customizer.withDefaults());
        return security.build();
    }
}
