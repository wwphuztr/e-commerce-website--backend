package com.nails.api.form.customer;

import com.nails.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateCustomerProfileForm {

    @ApiModelProperty(name = "customerEmail")
    private String customerEmail;

    @NotEmpty(message = "customerFullName cannot be null")
    @ApiModelProperty(required = true)
    private String customerFullName;

    @ApiModelProperty(name = "customerPassword")
    private String customerPassword;
    @ApiModelProperty(name = "oldPassword", required = true)
    private String oldPassword;

    @ApiModelProperty(name = "customerAvatarPath")
    private String customerAvatarPath;

    @ApiModelProperty(name = "birthday")
    private Date birthday;

    @ApiModelProperty(name = "sex")
    private Integer sex;
}
