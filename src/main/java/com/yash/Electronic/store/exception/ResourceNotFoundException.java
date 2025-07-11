package com.yash.Electronic.store.exception;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException {
    public  ResourceNotFoundException(){
        super("Resouce not found");
    }
    public  ResourceNotFoundException(String s){
        super(s);
    }
}
