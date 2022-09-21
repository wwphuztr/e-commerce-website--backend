package com.nails.api.form.orders;

import com.nails.api.storage.model.Orders;
import com.nails.api.storage.model.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CreateOrdersDetailForm {

    @NotNull(message = "productId cannot be null")
    @ApiModelProperty(required = true)
    private Long productId;

    @NotNull(message = "amount cannot be null")
    @ApiModelProperty(required = true)
    @Min(0)
    private Integer amount;

    @ApiModelProperty(name = "note")
    private String note;
}
