package com.org.Electronic.store.exception;

public class BadApiRequest  extends RuntimeException{
    public  BadApiRequest(String message){
        super(message);
    }
    public BadApiRequest(){
        super("Bad Request Type");
    }
}
