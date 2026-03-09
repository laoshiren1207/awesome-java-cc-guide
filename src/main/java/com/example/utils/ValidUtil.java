package com.example.utils;


import com.example.common.exception.ValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Iterator;
import java.util.Set;


public class ValidUtil {

    public static <T> T validate(T obj) throws ValidException {
        valid(obj);
        return obj;
    }

    public static <T> T validate(T obj, Class<?>... groups) throws ValidException {
        valid(obj, groups);
        return obj;
    }

    private static <T> T validBeanProperties(T bean, String... properties) throws ValidException {
        validProperties(bean, properties);
        return bean;
    }

    private static void valid(Object bean) throws ValidException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);
        Iterator<ConstraintViolation<Object>> it = constraintViolations.iterator();
        if (it.hasNext()) {
            ConstraintViolation<Object> constraintViolation = it.next();
            throw new ValidException(500, constraintViolation.getMessage());
        }
    }

    private static void valid(Object bean, Class<?>... groups) throws ValidException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean, groups);
        Iterator<ConstraintViolation<Object>> it = constraintViolations.iterator();
        if (it.hasNext()) {
            ConstraintViolation<Object> constraintViolation = it.next();
            throw new ValidException(500, constraintViolation.getMessage());
        }
    }

    private static void validProperties(Object bean, String... properties) throws ValidException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        for (String property : properties) {
            Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(bean, property);
            Iterator<ConstraintViolation<Object>> it = constraintViolations.iterator();
            if (it.hasNext()) {
                ConstraintViolation<Object> constraintViolation = it.next();
                throw new ValidException(500, constraintViolation.getMessage());
            }
        }
    }
}
