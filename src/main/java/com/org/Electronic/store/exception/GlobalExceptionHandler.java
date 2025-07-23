package com.yash.Electronic.store.exception;

import com.yash.Electronic.store.dtos.ApiResponseClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseClass> ResourceNotFoundExceptiom(ResourceNotFoundException ex){
        ApiResponseClass response = ApiResponseClass.builder()
                .message("Some went wrong")
                .status(HttpStatus.NOT_FOUND)
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> map = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String error = objectError.getDefaultMessage();
            String field  = ((FieldError)objectError).getField();
            map.put(field,error);
        });
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseClass> ResourceNotFoundExceptiom(BadApiRequest ex){
        ApiResponseClass response = ApiResponseClass.builder()
                .message("Some went wrong")
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
