package com.nails.api.form.province;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateProvinceForm {

    @NotEmpty(message = "provinceName cannot be null")
    @ApiModelProperty(required = true)
    private String provinceName;

    private Long parentId;
}
