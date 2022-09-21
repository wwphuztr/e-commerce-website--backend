package com.nails.api.form.orders;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateOrdersClientForm {

    @NotEmpty(message = "ordersAddress cannot be null")
    @ApiModelProperty(required = true)
    private String ordersAddress;

    @NotEmpty(message = "receiverName cannot be null")
    @ApiModelProperty(required = true)
    private String receiverName;

    @NotEmpty(message = "receiverPhone cannot be null")
    @ApiModelProperty(required = true)
    private String receiverPhone;

    @NotEmpty(message = "ordersDetailDtoList cannot be null")
    @ApiModelProperty(required = true)
    private List<@Valid CreateOrdersDetailForm> ordersDetailDtos;

    @NotNull(message = "paymentMethod cannot be null")
    @ApiModelProperty(required = true)
    private Integer paymentMethod;
}
