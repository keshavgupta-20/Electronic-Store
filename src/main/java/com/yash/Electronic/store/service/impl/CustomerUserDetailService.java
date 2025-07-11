package com.yash.Electronic.store.service.impl;

import com.yash.Electronic.store.exception.ResourceNotFoundException;
import com.yash.Electronic.store.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailService  implements UserDetailsService {
  @Autowired
   private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username).orElseThrow(()-> new ResourceNotFoundException("User not found with given id"));
    }
}
