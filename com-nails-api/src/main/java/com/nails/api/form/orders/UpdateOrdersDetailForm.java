package com.nails.api.form.orders;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UpdateOrdersDetailForm {

    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @NotNull(message = "ordersId cannot be null")
    @ApiModelProperty(required = true)
    private Long ordersId;

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
