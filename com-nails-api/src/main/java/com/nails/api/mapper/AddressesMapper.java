package com.nails.api.mapper;

import com.nails.api.dto.addresses.AddressesDto;
import com.nails.api.form.addresses.CreateAddressesForm;
import com.nails.api.form.addresses.UpdateAddressesForm;
import com.nails.api.storage.model.Addresses;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = { ProvinceMapper.class })
public interface AddressesMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "commune", target = "communeDto", qualifiedByName = "provinceGetAutoCompleteMapping")
    @Mapping(source = "district", target = "districtDto", qualifiedByName = "provinceGetAutoCompleteMapping")
    @Mapping(source = "province", target = "provinceDto", qualifiedByName = "provinceGetAutoCompleteMapping")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("addressMapping")
    AddressesDto fromEntityToAddressesDto(Addresses addresses);

    @IterableMapping(elementTargetType = AddressesDto.class, qualifiedByName = "addressMapping")
    List<AddressesDto> fromEntityListToAddressesDtoList(List<Addresses> content);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "commune", target = "communeDto", qualifiedByName = "provinceGetAutoCompleteMapping")
    @Mapping(source = "district", target = "districtDto", qualifiedByName = "provinceGetAutoCompleteMapping")
    @Mapping(source = "province", target = "provinceDto", qualifiedByName = "provinceGetAutoCompleteMapping")
    @BeanMapping(ignoreByDefault = true)
    @Named("addressMappingAutoComplete")
    AddressesDto fromEntityToAddressesDtoAutoComplete(Addresses addresses);

    @IterableMapping(elementTargetType = AddressesDto.class, qualifiedByName = "addressMappingAutoComplete")
    List<AddressesDto> fromEntityListToAdminDtoListAutoComplete(List<Addresses> content);

    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "communeId", target = "commune.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "provinceId", target = "province.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Addresses fromCreateAddressesFormToEntity(CreateAddressesForm createAddressesForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "communeId", target = "commune.id")
    @Mapping(source = "districtId", target = "district.id")
    @Mapping(source = "provinceId", target = "province.id")
    @BeanMapping(ignoreByDefault = true)
    @Named("addressUpdateMapping")
    void fromUpdateAddressesFormToEntity(UpdateAddressesForm updateAddressesForm, @MappingTarget Addresses addresses);
}

