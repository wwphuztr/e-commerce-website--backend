package com.nails.api.form.addresses;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateAddressesForm {

    @NotNull(message = "customerId cannot be null")
    @ApiModelProperty(required = true)
    private Long customerId;

    @NotEmpty(message = "name cannot be null")
    @ApiModelProperty(required = true)
    private String name;

    @NotEmpty(message = "phone cannot be null")
    @ApiModelProperty(required = true)
    private String phone;

    @NotEmpty(message = "address cannot be null")
    @ApiModelProperty(required = true)
    private String address;

    @NotNull(message = "districtId cannot be null")
    @ApiModelProperty(required = true)
    private Long districtId;

    @NotNull(message = "communeId cannot be null")
    @ApiModelProperty(required = true)
    private Long communeId;

    @NotNull(message = "provinceId cannot be null")
    @ApiModelProperty(required = true)
    private Long provinceId;
}
