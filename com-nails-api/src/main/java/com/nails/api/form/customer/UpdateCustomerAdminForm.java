package com.nails.api.form.customer;

import com.nails.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UpdateCustomerAdminForm {

    @NotNull(message = "id cannot be null")
    @ApiModelProperty(required = true)
    private Long id;

    @ApiModelProperty(required = true)
    private String customerEmail;

    @ApiModelProperty(required = true)
    private String customerFullName;

    @ApiModelProperty(name = "customerPassword")
    private String customerPassword;

    @NotEmpty(message = "customerPhone cannot be null")
    @ApiModelProperty(required = true)
    private String customerPhone;

    @ApiModelProperty(name = "customerAvatarPath")
    private String customerAvatarPath;

    @ApiModelProperty(required = true)
    private String customerAddress;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;

    @ApiModelProperty(required = true)
    private Date birthday;

    @ApiModelProperty(required = true)
    private Integer sex;

    private String note;

    private Boolean isLoyalty;
    private Integer loyaltyLevel;
    private Integer saleOff;
}
