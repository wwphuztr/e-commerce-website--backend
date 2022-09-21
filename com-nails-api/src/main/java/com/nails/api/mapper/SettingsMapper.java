/*
 * File Created: Saturday, 30th January 2021 3:23:11 pm
 * Author: Bui Si Quan (18110041@student.hcmute.edu.vn)
 * -----
 */
package com.nails.api.mapper;

import java.util.List;

import com.nails.api.dto.settings.SettingsAdminDto;
import com.nails.api.dto.settings.SettingsDto;
import com.nails.api.form.settings.CreateSettingsForm;
import com.nails.api.form.settings.UpdateSettingsForm;
import com.nails.api.storage.model.Settings;

import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SettingsMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "key", target = "key")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "editable", target = "editable")
    @Mapping(source = "kind", target = "kind")
    @BeanMapping(ignoreByDefault = true)
    @Named("getMapping")
    SettingsDto fromEntityToDto(Settings settings);

    @IterableMapping(elementTargetType = SettingsDto.class, qualifiedByName = "getMapping")
    List<SettingsDto> fromEntityListToDtoList(List<Settings> list);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "key", target = "key")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "group", target = "group")
    @Mapping(source = "editable", target = "editable")
    @Mapping(source = "kind", target = "kind")
    SettingsAdminDto fromEntityToAdminDto(Settings settings);

    @IterableMapping(elementTargetType = SettingsAdminDto.class)
    List<SettingsAdminDto> fromEntityListToAdminDtoList(List<Settings> list);

    // create form to entity
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "settingKey", target = "key")
    @Mapping(source = "settingValue", target = "value")
    @Mapping(source = "settingGroup", target = "group")
    @Mapping(source = "editable", target = "editable")
    @Mapping(source = "kind", target = "kind")
    Settings fromCreateSettingsToSetting(CreateSettingsForm createSettingsForm);

    // update form to entity
    @Mapping(source = "name", target = "settings.name")
    @Mapping(source = "description", target = "settings.description")
    @Mapping(source = "settingKey", target = "settings.key")
    @Mapping(source = "settingValue", target = "settings.value")
    @Mapping(source = "settingGroup", target = "settings.group")
    Settings fromUpdateSettingsToSetting(UpdateSettingsForm updateSettingsForm,
            @MappingTarget Settings settings);

}
