package com.nails.api.mapper;

import com.nails.api.dto.orders.OrdersDto;
import com.nails.api.form.orders.CreateOrdersClientForm;
import com.nails.api.form.orders.CreateOrdersForm;
import com.nails.api.form.orders.UpdateInfoOrdersForm;
import com.nails.api.form.orders.UpdateOrdersForm;
import com.nails.api.storage.model.Orders;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = { CustomerMapper.class})
public interface OrdersMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "ordersCode")
    @Mapping(source = "customer", target = "customerDto", qualifiedByName = "adminAutoCompleteMapping")
    @Mapping(source = "receiverName", target = "receiverName")
    @Mapping(source = "receiverPhone", target = "receiverPhone")
    @Mapping(source = "saleOff", target = "ordersSaleOff")
    @Mapping(source = "totalMoney", target = "ordersTotalMoney")
    @Mapping(source = "state", target = "ordersState")
    @Mapping(source = "prevState", target = "ordersPrevState")
    @Mapping(source = "document", target = "ordersDocument")
    @Mapping(source = "address", target = "ordersAddress")
    @Mapping(source = "vat", target = "ordersVat")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    OrdersDto fromEntityToAdminDto(Orders orders);

    @IterableMapping(elementTargetType = OrdersDto.class, qualifiedByName = "adminGetMapping")
    List<OrdersDto> fromEntityListToOrdersDtoList(List<Orders> orders);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "code", target = "ordersCode")
    @Mapping(source = "receiverName", target = "receiverName")
    @Mapping(source = "receiverPhone", target = "receiverPhone")
    @Mapping(source = "saleOff", target = "ordersSaleOff")
    @Mapping(source = "totalMoney", target = "ordersTotalMoney")
    @Mapping(source = "state", target = "ordersState")
    @Mapping(source = "prevState", target = "ordersPrevState")
    @Mapping(source = "address", target = "ordersAddress")
    @Mapping(source = "vat", target = "ordersVat")
    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "createdDate", target = "createdDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetMapping")
    OrdersDto fromEntityToClientDto(Orders orders);

    @IterableMapping(elementTargetType = OrdersDto.class, qualifiedByName = "clientGetMapping")
    List<OrdersDto> fromEntityListToClientOrdersDtoList(List<Orders> orders);

    @Mapping(source = "paymentMethod", target = "paymentMethod")
    @Mapping(source = "receiverName", target = "receiverName")
    @Mapping(source = "receiverPhone", target = "receiverPhone")
    @Mapping(source = "ordersAddress", target = "address")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientCreateMapping")
    Orders fromCreateOrdersClientFormToEntity(CreateOrdersClientForm createOrdersClientForm );

    @Mapping(source = "ordersSaleOff", target = "saleOff")
    @Mapping(source = "ordersDocument", target = "document")
    @Mapping(source = "ordersAddress", target = "address")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Orders fromCreateOrdersAdminFormToEntity(CreateOrdersForm createOrdersForm );

    @Mapping(source = "id", target = "id")
    @Mapping(source = "ordersState", target = "state")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateOrdersAdminFormToEntity(UpdateOrdersForm updateOrdersForm, @MappingTarget Orders orders);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "ordersAddress", target = "address")
    @Mapping(source = "ordersSaleOff", target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateCustomerInfoMapping")
    void fromUpdateOrdersInfoAdminFormToEntity(UpdateInfoOrdersForm updateInfoOrdersForm, @MappingTarget Orders orders);
}
