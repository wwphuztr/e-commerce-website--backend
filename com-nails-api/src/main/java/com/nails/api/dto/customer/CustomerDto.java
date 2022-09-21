package com.nails.api.dto.customer;

import com.nails.api.dto.ABasicAdminDto;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerDto extends ABasicAdminDto {
    //private String customerUsername;
    private String customerEmail;
    private String customerFullName;
    private String customerPhone;
    private String customerAvatarPath;
    private String customerAddress;
    private Date birthday;
    private Integer sex;
    private String note;
    private Date loyaltyDate;
    private Boolean isLoyalty;
    private Integer loyaltyLevel;
    private Integer saleOff;
}
