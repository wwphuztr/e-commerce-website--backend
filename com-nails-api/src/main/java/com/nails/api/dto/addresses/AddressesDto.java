package com.nails.api.dto.addresses;

import com.nails.api.dto.ABasicAdminDto;
import com.nails.api.dto.province.ProvinceDto;
import lombok.Data;

@Data
public class AddressesDto extends ABasicAdminDto {
    private Long customerId;
    private String name;
    private String phone;
    private String address;
    private ProvinceDto districtDto;
    private ProvinceDto communeDto;
    private ProvinceDto provinceDto;
}
