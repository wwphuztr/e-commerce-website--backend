package com.nails.api.validation.impl;

import com.nails.api.constant.NailsConstant;
import com.nails.api.validation.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class StatusValidation  implements ConstraintValidator<Status, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(Status constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }
    @Override
    public boolean isValid(Integer status, ConstraintValidatorContext constraintValidatorContext) {
        if(status == null && allowNull){
            return true;
        }
        if(!Objects.equals(status, NailsConstant.STATUS_ACTIVE)
                && !Objects.equals(status, NailsConstant.STATUS_LOCK)
                && !Objects.equals(status, NailsConstant.STATUS_DELETE)
                && !Objects.equals(status, NailsConstant.STATUS_PENDING)){
            return false;
        }
        return true;
    }
}
