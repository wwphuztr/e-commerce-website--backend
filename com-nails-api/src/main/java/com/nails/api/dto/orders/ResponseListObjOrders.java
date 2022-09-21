package com.nails.api.dto.orders;

import com.nails.api.dto.ResponseListObj;
import lombok.Data;

@Data
public class ResponseListObjOrders<T> extends ResponseListObj<T> {
    private Double orderTotalPrice;
}
