package com.nails.api.mapper;

import com.nails.api.dto.customer.CustomerDto;
import com.nails.api.form.customer.CreateCustomerForm;
import com.nails.api.form.customer.CustomerRegisterForm;
import com.nails.api.form.customer.UpdateCustomerAdminForm;
import com.nails.api.form.customer.UpdateCustomerProfileForm;
import com.nails.api.storage.model.Customer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    //@Mapping(source = "customerUsername", target = "account.username")
    @Mapping(source = "customerEmail", target = "account.email")
    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "customerPassword", target = "account.password")
    @Mapping(source = "customerPhone", target = "account.phone")
    @Mapping(source = "customerAvatarPath", target = "account.avatarPath")
    @Mapping(source = "customerAddress", target = "address")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "isLoyalty", target = "isLoyalty")
    @Mapping(source = "loyaltyLevel", target = "loyaltyLevel")
    @Mapping(source = "saleOff", target = "saleOff")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Customer fromCreateCustomerFormToEntity(CreateCustomerForm createCustomerAdminForm);

    @Mapping(source = "customerEmail", target = "account.email")
    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "customerPassword", target = "account.password")
    @Mapping(source = "customerPhone", target = "account.phone")
    @Mapping(source = "customerAvatarPath", target = "account.avatarPath")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "customerAddress", target = "address")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "note", target = "note")
    @Mapping(source = "isLoyalty", target = "isLoyalty")
    @Mapping(source = "loyaltyLevel", target = "loyaltyLevel")
    @Mapping(source = "saleOff", target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateCustomerAdminFromToEntity(UpdateCustomerAdminForm updateCustomerAdminForm, @MappingTarget Customer customer);

    @Mapping(source = "id", target = "id")
    //@Mapping(source = "account.username", target = "customerUsername")
    @Mapping(source = "account.email", target = "customerEmail")
    @Mapping(source = "account.fullName", target = "customerFullName")
    @Mapping(source = "account.phone", target = "customerPhone")
    @Mapping(source = "account.avatarPath", target = "customerAvatarPath")
    @Mapping(source = "address", target = "customerAddress")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminMapping")
    CustomerDto fromEntityToAdminDto(Customer customer);

    @Mapping(source = "id", target = "id")
    //@Mapping(source = "account.username", target = "customerUsername")
    @Mapping(source = "account.email", target = "customerEmail")
    @Mapping(source = "account.fullName", target = "customerFullName")
    @Mapping(source = "account.phone", target = "customerPhone")
    @Mapping(source = "account.avatarPath", target = "customerAvatarPath")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "sex", target = "sex")
    @Mapping(source = "address", target = "customerAddress")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientMapping")
    CustomerDto fromEntityToClientDto(Customer customer);

    @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "adminMapping")
    List<CustomerDto> fromEntityListToAdminDtoList(List<Customer> content);

    @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "clientMapping")
    List<CustomerDto> fromEntityListToClientDtoList(List<Customer> content);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account.email", target = "customerEmail")
    @Mapping(source = "account.fullName", target = "customerFullName")
    @Mapping(source = "account.phone", target = "customerPhone")
    @Mapping(source = "address", target = "customerAddress")
    @Mapping(source = "saleOff", target = "saleOff")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminAutoCompleteMapping")
    CustomerDto fromEntityToAdminDtoAutocomplete(Customer customer);

    @IterableMapping(elementTargetType = CustomerDto.class, qualifiedByName = "adminAutoCompleteMapping")
    List<CustomerDto> fromEntityListToAdminDtoListAutocomplete(List<Customer> content);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "account.email", target = "customerEmail")
    @Mapping(source = "account.fullName", target = "customerFullName")
    @Mapping(source = "account.phone", target = "customerPhone")
    @Mapping(source = "account.avatarPath", target = "customerAvatarPath")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "sex", target = "sex")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientProfileMapping")
    CustomerDto fromEntityToClientProfileDto(Customer customer);

    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "customerPhone", target = "account.phone")
    @BeanMapping(ignoreByDefault = true)
    @Named("registerMapping")
    Customer fromCustomerRegisterFormToEntity(CustomerRegisterForm customerRegisterForm);

    @Mapping(source = "customerEmail", target = "account.email")
    @Mapping(source = "customerFullName", target = "account.fullName")
    @Mapping(source = "birthday", target = "birthday")
    @Mapping(source = "sex", target = "sex")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateCustomerProfileFormToEntity(UpdateCustomerProfileForm updateCustomerProfileForm, @MappingTarget Customer customer);
}
