package com.lcwd.electronic.store.Electronic.store.Controller;

import com.lcwd.electronic.store.Electronic.store.Entites.User;
import com.lcwd.electronic.store.Electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.Electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.Electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.Electronic.store.security.JwtHelper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService  userDetailsService;

    @Autowired
    private ModelMapper modelMapper;
    private Logger logger  = LoggerFactory.getLogger(AuthenticationController.class);
    @PostMapping("/generate-token")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        logger.info("UserName{} , Password{}", request.getEmail(), request.getPassword());
       this.doAuthenticate(request.getEmail(), request.getPassword());
        User userDetails = (User) userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtHelper.generateToken(userDetails);
        JwtResponse jwtResponse = JwtResponse.builder().token(token).userDto(modelMapper.map(userDetails, UserDto.class)).build();

        return ResponseEntity.ok(jwtResponse);
    }

    public void doAuthenticate(String email, String password) {
        try{
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);
        }
        catch (BadCredentialsException ex){
            throw new BadCredentialsException("Invalid UserName and Password");
        }
    }
}
