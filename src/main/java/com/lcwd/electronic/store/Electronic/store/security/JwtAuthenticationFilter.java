package com.lcwd.electronic.store.Electronic.store.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

   @Autowired
   private UserDetailsService userDetailsService;

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //api se pehla chalega verify karna ka liya
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header {}", requestHeader);
        String username = null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")){
            //sab theek hai
            token = requestHeader.substring(7);
            try{
                username = jwtHelper.getUserFromToken(token);
                logger.info("Token Username :  {}", username);

            }
            catch (IllegalArgumentException ex){
                logger.info("Illegal Argument while fetching the username" + ex.getMessage());
            }
            catch (ExpiredJwtException ex){
                logger.info("Giving Jwt is Expired" + ex.getMessage());
            }
            catch (MalformedJwtException ex){
                logger.info("Some changed has done in Token"+ ex.getMessage());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else{
            logger.warn("Invalid Header !! Header is not starting with Bearer");
        }
        if (username !=  null && SecurityContextHolder.getContext().getAuthentication() == null){
            //username kuch hai
            UserDetails userDetails= userDetailsService.loadUserByUsername(username);
            if (username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)){
                //token valid
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //security context ka andar authentication set karo
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        filterChain.doFilter(request, response);
    }
}
