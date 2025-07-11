package com.yash.Electronic.store.config;

import com.yash.Electronic.store.security.OAuthAuthenticationSuccessHandler;
import com.yash.Electronic.store.security.SecurityCustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private OAuthAuthenticationSuccessHandler  handler;


    @Autowired
    private SecurityCustomUserDetailService userDetailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers("/img/**", "/css/**", "/js/**", "/webjars/**").permitAll()
                            .requestMatchers("/ElectroHub/login", "/ElectroHub/register", "/ElectroHub/productss", "/ElectroHub/deals","/ElectroHub/user/do-register", "/ElectroHub/").permitAll()
                            .requestMatchers("/ElectroHub/wishlist").hasAnyRole("USER", "ADMIN")
                            .requestMatchers("/ElectroHub/admin/dashboard").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .formLogin(formLogin -> {
                    formLogin
                            .loginPage("/ElectroHub/login")
                            .loginProcessingUrl("/authenticate")
                            .defaultSuccessUrl("/ElectroHub/", true) // ✅ Redirect, not forward
                            .failureUrl("/ElectroHub/login?error=true")
                            .usernameParameter("email")
                            .passwordParameter("password");

                });
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/ElectroHub/login?logout=true");
        });

        //oauth Configuration
        httpSecurity.oauth2Login(oauth2 ->
                oauth2
                        .loginPage("/ElectroHub/login")
                        .successHandler(handler)

        );

        return httpSecurity.build();
    }

}
