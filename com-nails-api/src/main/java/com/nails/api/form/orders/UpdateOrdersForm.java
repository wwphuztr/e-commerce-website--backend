package com.nails.api.form.orders;

import com.nails.api.validation.OrdersState;
import lombok.Data;

@Data
public class UpdateOrdersForm {

    private Long id;

    @OrdersState
    private Integer ordersState;
}