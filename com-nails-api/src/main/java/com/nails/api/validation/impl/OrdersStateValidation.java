package com.nails.api.validation.impl;

import com.nails.api.constant.NailsConstant;
import com.nails.api.validation.OrdersState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class OrdersStateValidation implements ConstraintValidator<OrdersState, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(OrdersState constraintAnnotation) { allowNull = constraintAnnotation.allowNull(); }

    @Override
    public boolean isValid(Integer ordersState, ConstraintValidatorContext constraintValidatorContext) {
        if(ordersState == null && allowNull) {
            return true;
        }
        if(!Objects.equals(ordersState, NailsConstant.ORDERS_STATE_CREATED)
                && !Objects.equals(ordersState, NailsConstant.ORDERS_STATE_ACCEPTED)
                && !Objects.equals(ordersState, NailsConstant.ORDERS_STATE_SHIPPING)
                && !Objects.equals(ordersState, NailsConstant.ORDERS_STATE_DONE)
                && !Objects.equals(ordersState, NailsConstant.ORDERS_STATE_CANCEL)) {
            return false;
        }
        return true;
    }
}
