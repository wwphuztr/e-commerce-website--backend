package com.nails.api.form.category;

import com.nails.api.validation.CategoryKind;
import com.nails.api.validation.Status;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateCategoryForm {

    @NotEmpty(message = "categoryName cannot be null")
    @ApiModelProperty(required = true)
    private String categoryName;

    @NotEmpty(message = "categoryDescription cannot be null")
    @ApiModelProperty(required = true)
    private String categoryDescription;

    @ApiModelProperty(name = "categoryImage")
    private String categoryImage;

    @NotNull(message = "categoryOrdering cannot be null")
    @ApiModelProperty(required = true)
    private Integer categoryOrdering;

    @CategoryKind
    @NotNull(message = "categoryKind cannot be null")
    @ApiModelProperty(required = true)
    private Integer categoryKind;

    @ApiModelProperty(name = "parentId")
    private Long parentId;
}
