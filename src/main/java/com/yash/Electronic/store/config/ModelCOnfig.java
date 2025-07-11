package com.yash.Electronic.store.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelCOnfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
