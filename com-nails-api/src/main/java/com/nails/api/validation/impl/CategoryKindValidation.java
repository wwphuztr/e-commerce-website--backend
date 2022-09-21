package com.nails.api.validation.impl;

import com.nails.api.constant.NailsConstant;
import com.nails.api.validation.CategoryKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CategoryKindValidation implements ConstraintValidator<CategoryKind, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(CategoryKind constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer categoryKind, ConstraintValidatorContext constraintValidatorContext) {
        if(categoryKind == null && allowNull) {
            return true;
        }
        if(!Objects.equals(categoryKind, NailsConstant.CATEGORY_KIND_IMPORT)
                && !Objects.equals(categoryKind, NailsConstant.CATEGORY_KIND_EXPORT)
                && !Objects.equals(categoryKind, NailsConstant.CATEGORY_KIND_PRODUCT)
                && !Objects.equals(categoryKind, NailsConstant.CATEGORY_KIND_NEWS)) {
            return false;
        }
        return true;
    }
}
