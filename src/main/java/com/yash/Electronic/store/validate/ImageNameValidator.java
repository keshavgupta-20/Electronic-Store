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
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        logger.info("Message from isValid : {}", value);
        if (value == null || value.isBlank()) {
            return false; // or true, depending on whether null is acceptable
        }

        // Continue with image name validation logic
        return value.endsWith(".jpg") || value.endsWith(".png") || value.endsWith(".jpeg");

    }
}