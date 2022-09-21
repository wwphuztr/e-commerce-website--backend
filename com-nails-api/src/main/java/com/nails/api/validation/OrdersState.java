package com.nails.api.validation;

import com.nails.api.validation.impl.OrdersStateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrdersStateValidation.class)
@Documented
public @interface OrdersState {
    boolean allowNull() default false;
    String message() default  "State invalid.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
