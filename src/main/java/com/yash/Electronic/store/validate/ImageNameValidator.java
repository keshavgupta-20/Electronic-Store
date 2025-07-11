package com.yash.Electronic.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImageValidator, String> {


    Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);
    @Override
    public void initialize(ImageValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        logger.info("Message from isValid : {}", s);
        if (s.isBlank()){
            return false;
        }
        else{
        return true;
        }
    }
}