package com.yash.Electronic.store.exception;

import com.yash.Electronic.store.dtos.ApiResponseClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        ex.printStackTrace();

        return "error/404"; // You can create templates/error/404.html
    }

    // Handle Spring's 404 exception (for unmapped URLs or missing static resources)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "Page or resource not found.");
        ex.printStackTrace();
        return "error/404";
    }

    // Catch-all for any other exceptions
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("message", "An unexpected error occurred.");
        return "error/500"; // Create templates/error/500.html
    }
}

