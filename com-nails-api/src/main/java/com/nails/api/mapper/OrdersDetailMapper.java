package com.nails.api.mapper;

import com.nails.api.dto.orders.OrdersDetailDto;
import com.nails.api.form.orders.CreateOrdersDetailForm;
import com.nails.api.form.orders.UpdateOrdersDetailForm;
import com.nails.api.storage.model.Orders;
import com.nails.api.storage.model.OrdersDetail;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = { ProductMapper.class })
public interface OrdersDetailMapper {
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    OrdersDetail fromCreateOrdersDetailAdminFormToEntity(CreateOrdersDetailForm createOrdersDetailForm );

    @IterableMapping(elementTargetType = OrdersDetail.class, qualifiedByName = "adminCreateMapping")
    List<OrdersDetail> fromCreateOrdersDetailAdminFormListToEntityList(List<CreateOrdersDetailForm> createOrdersDetailFormList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "ordersId", target = "orders.id")
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateOrdersDetailAdminFormToEntity(UpdateOrdersDetailForm updateOrdersDetailForm, @MappingTarget OrdersDetail ordersDetail);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "orders.id", target = "ordersId")
    @Mapping(source = "product", target = "productDto", qualifiedByName = "adminGetAutoCompleteMapping")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "price", target = "price")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    OrdersDetailDto fromEntityToAdminDto(OrdersDetail ordersDetail);

    @IterableMapping(elementTargetType = OrdersDetailDto.class, qualifiedByName = "adminGetMapping")
    List<OrdersDetailDto> fromEntityListToOrdersDetailDtoList(List<OrdersDetail> ordersDetails);
}
