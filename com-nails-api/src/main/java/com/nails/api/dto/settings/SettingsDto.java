/*
 * File Created: Friday, 29th January 2021 11:54:43 pm
 * Author: Bui Si Quan (18110041@student.hcmute.edu.vn)
 * -----
 */
package com.nails.api.dto.settings;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SettingsDto {

    @ApiModelProperty(name = "id")
    private Long id;

    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "key")
    private String key;

    @ApiModelProperty(name = "value")
    private String value;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "group")
    private String group;

    @ApiModelProperty(name = "editable")
    private boolean editable;

    @ApiModelProperty(name = "kind")
    private Integer kind;

}
