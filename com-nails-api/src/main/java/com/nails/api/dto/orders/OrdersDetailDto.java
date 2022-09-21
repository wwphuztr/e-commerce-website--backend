package com.nails.api.dto.orders;

import com.nails.api.dto.ABasicAdminDto;
import com.nails.api.dto.product.ProductAdminDto;
import lombok.Data;

@Data
public class OrdersDetailDto extends ABasicAdminDto {

    private Long id;
    private Long ordersId;
    private ProductAdminDto productDto;
    private Double price;
    private Integer amount;
    private String note;

    private Integer kind;
    private Double value;
    private Double collaboratorCommission = 0d;
}
