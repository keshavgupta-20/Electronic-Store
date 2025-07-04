package com.lcwd.electronic.store.Exception;

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
