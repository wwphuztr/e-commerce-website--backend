package com.nails.api.form.customer;

import com.nails.api.storage.model.Account;
import com.nails.api.validation.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CreateCustomerForm {
//    @NotEmpty(message = "customerUsername cannot be null")
//    @ApiModelProperty(required = true)
//    private String customerUsername;

    @NotEmpty(message = "customerEmail cannot be null")
    @ApiModelProperty(required = true)
    private String customerEmail;

    @NotEmpty(message = "customerFullName cannot be null")
    @ApiModelProperty(required = true)
    private String customerFullName;

    @NotEmpty(message = "customerPassword cannot be null")
    @ApiModelProperty(required = true)
    private String customerPassword;

    @NotEmpty(message = "customerPhone cannot be null")
    @ApiModelProperty(required = true)
    private String customerPhone;

    @ApiModelProperty(name = "customerAvatarPath")
    private String customerAvatarPath;

    @NotEmpty(message = "customerAddress cannot be null")
    @ApiModelProperty(required = true)
    private String customerAddress;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;

    @NotNull(message = "birthday cannot be null")
    @ApiModelProperty(required = true)
    private Date birthday;

    @NotNull(message = "sex cannot be null")
    @ApiModelProperty(required = true)
    private Integer sex;

    private String note;

    @NotNull(message = "isLoyalty cannot be null")
    @ApiModelProperty(required = true)
    private Boolean isLoyalty;
    private Integer loyaltyLevel;
    private Integer saleOff;
}
