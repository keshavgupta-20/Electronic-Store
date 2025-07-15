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
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

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
                            .requestMatchers("/ElectroHub/login", "/ElectroHub/register", "/ElectroHub/products", "/ElectroHub/deals", "/ElectroHub/user/do-register", "/ElectroHub/").permitAll()
                            .requestMatchers("/ElectroHub/cart/add").authenticated()  // ✅ Allow only logged-in users
                            .anyRequest().authenticated();
                })
                .formLogin(formLogin -> {
                    formLogin
                            .loginPage("/ElectroHub/login")
                            .loginProcessingUrl("/authenticate")
                            .defaultSuccessUrl("/ElectroHub/", true)
                            .failureUrl("/ElectroHub/login?error=true")
                            .usernameParameter("email")
                            .passwordParameter("password");
                })
                .logout(logoutForm -> {
                    logoutForm
                            .logoutUrl("/do-logout")
                            .logoutSuccessUrl("/ElectroHub/login?logout=true")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                            .deleteCookies("JSESSIONID");
                })
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/ElectroHub/login")
                        .successHandler(handler)
                )
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())// ✅ CSRF exempt for AJAX
                );

        return httpSecurity.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//    httpSecurity
//            .authorizeHttpRequests(auth -> auth
//                    .anyRequest().permitAll() // Allow all URLs
//            )
//            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
//            .formLogin(AbstractHttpConfigurer::disable) // Disable form login
//            .httpBasic(AbstractHttpConfigurer::disable) // Disable basic auth
//            .logout(AbstractHttpConfigurer::disable); // Disable logout
//
//    return httpSecurity.build();
//}



}
