package com.nails.api.dto.orders;

import com.nails.api.dto.ABasicAdminDto;
import com.nails.api.dto.customer.CustomerDto;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends ABasicAdminDto {

    private Long id;
    private CustomerDto customerDto;
    private Integer ordersSaleOff;
    private Double ordersTotalMoney;
    private Integer ordersState;
    private Integer ordersPrevState;
    private String ordersDocument;
    private String ordersAddress;
    private Integer ordersVat;
    private String ordersCode;
    private Integer paymentMethod;
    private String receiverName;
    private String receiverPhone;
    private List<OrdersDetailDto> ordersDetailDtos;
}
