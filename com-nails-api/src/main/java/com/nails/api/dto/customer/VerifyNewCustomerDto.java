package com.nails.api.dto.customer;

import lombok.Data;

@Data
public class VerifyNewCustomerDto {
    private String customerEmail;
    private Long id;
}
