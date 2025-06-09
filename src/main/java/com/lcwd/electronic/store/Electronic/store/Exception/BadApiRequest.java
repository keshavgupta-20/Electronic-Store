package com.lcwd.electronic.store.Electronic.store.Exception;

public class BadApiRequest  extends RuntimeException{
    public  BadApiRequest(String message){
        super(message);
    }
    public BadApiRequest(){
        super("Bad Request Type");
    }
}
