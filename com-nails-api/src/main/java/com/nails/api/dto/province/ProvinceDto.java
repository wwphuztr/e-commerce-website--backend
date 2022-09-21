package com.nails.api.dto.province;

import com.nails.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class ProvinceDto extends ABasicAdminDto {
    private Long id;
    private String provinceName;
    private Long parentId;
    private String kind;
}
