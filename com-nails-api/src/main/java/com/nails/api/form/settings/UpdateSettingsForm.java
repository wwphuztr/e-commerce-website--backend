/*
 * File Created: Saturday, 30th January 2021 11:22:39 pm
 * Author: Bui Si Quan (18110041@student.hcmute.edu.vn)
 * -----
 */
package com.nails.api.form.settings;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
public class UpdateSettingsForm {
    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;
    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @NotEmpty(message = "settingKey cant not be null")
    @ApiModelProperty(name = "settingKey", required = true)
    private String settingKey;
    @NotEmpty(message = "settingValue cant not be null")
    @ApiModelProperty(name = "settingValue", required = true)
    private String settingValue;
    @NotEmpty(message = "settingGroup cant not be null")
    @ApiModelProperty(name = "settingGroup", required = true)
    private String settingGroup;
}
