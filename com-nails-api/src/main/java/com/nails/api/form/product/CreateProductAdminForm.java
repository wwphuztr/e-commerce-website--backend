package com.nails.api.form.product;

import com.nails.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateProductAdminForm {

    @NotEmpty(message = "productName cannot be null")
    @ApiModelProperty(required = true)
    private String productName;

    @NotNull(message = "productPrice cannot be null")
    @ApiModelProperty(required = true)
    private Long productPrice;

    @ApiModelProperty(name = "productImage")
    private String productImage;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "shortDescription")
    private String shortDescription;

    @Status
    @NotNull(message = "status cannot be null")
    @ApiModelProperty(required = true)
    private Integer status;

    @NotNull(message = "categoryId cannot be null")
    @ApiModelProperty(required = true)
    private Long categoryId;

    @ApiModelProperty(name = "labelColor")
    private String labelColor;

    @ApiModelProperty(name = "parentId")
    private Long parentId;

    @ApiModelProperty(name = "saleoff")
    private Integer saleoff;

//    @ApiModelProperty(name = "quantity")
//    private Integer quantity;
}
