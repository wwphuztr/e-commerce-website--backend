package com.nails.api.form.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CustomerRegisterForm {
    @NotEmpty(message = "phone cannot be null")
    @ApiModelProperty(required = true)
    private String customerPhone;

    @NotEmpty(message = "fullName cannot be null")
    @ApiModelProperty(required = true)
    private String customerFullName;

    @NotEmpty(message = "password cannot be null")
    @ApiModelProperty(required = true)
    private String customerPassword;

    @NotNull(message = "districtId cannot be null")
    @ApiModelProperty(required = true)
    private Long districtId;

    @NotNull(message = "communeId cannot be null")
    @ApiModelProperty(required = true)
    private Long communeId;

    @NotNull(message = "provinceId cannot be null")
    @ApiModelProperty(required = true)
    private Long provinceId;

    @NotEmpty(message = "address cannot be null")
    @ApiModelProperty(required = true)
    private String address;
}
