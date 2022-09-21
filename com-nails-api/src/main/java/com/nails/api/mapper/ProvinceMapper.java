package com.nails.api.mapper;

import com.nails.api.dto.province.ProvinceDto;
import com.nails.api.form.province.CreateProvinceForm;
import com.nails.api.form.province.UpdateProvinceForm;
import com.nails.api.storage.model.Province;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProvinceMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "provinceName")
    @Mapping(source = "parentProvince.id", target = "parentId")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("provinceGetMapping")
    ProvinceDto fromEntityToProvinceDto(Province province);

    @IterableMapping(elementTargetType = ProvinceDto.class, qualifiedByName = "provinceGetMapping")
    List<ProvinceDto> fromEntityListToProvinceDtoList(List<Province> provinces);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "provinceName")
    @Mapping(source = "parentProvince.id", target = "parentId")
    @BeanMapping(ignoreByDefault = true)
    @Named("provinceGetAutoCompleteMapping")
    ProvinceDto fromEntityToProvinceDtoAutoComplete(Province province);

    @IterableMapping(elementTargetType = ProvinceDto.class, qualifiedByName = "provinceGetAutoCompleteMapping")
    @Named("provinceGetAutoCompleteListMapping")
    List<ProvinceDto> fromEntityListToProvinceDtoListAutoComplete(List<Province> provinces);

    @Mapping(source = "provinceName", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("provinceCreateMapping")
    Province fromCreateProvinceFormToEntity(CreateProvinceForm createProvinceForm );

    @Mapping(source = "id", target = "id")
    @Mapping(source = "provinceName", target = "name")
    @BeanMapping(ignoreByDefault = true)
    @Named("provinceUpdateMapping")
    void fromUpdateProvinceFormToEntity(UpdateProvinceForm updateProvinceForm, @MappingTarget Province province);
}
