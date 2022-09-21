/*
 * File Created: Saturday, 30th January 2021 3:29:10 pm
 * Author: Bui Si Quan (18110041@student.hcmute.edu.vn)
 * -----
 */
package com.nails.api.controller;

import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.settings.SettingsAdminDto;
import com.nails.api.dto.settings.SettingsDto;
import com.nails.api.exception.UnauthorizationException;
import com.nails.api.form.settings.CreateSettingsForm;
import com.nails.api.form.settings.UpdateSettingsForm;
import com.nails.api.mapper.SettingsMapper;
import com.nails.api.storage.criteria.SettingsCriteria;
import com.nails.api.storage.model.Settings;
import com.nails.api.storage.repository.SettingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping("/v1/settings")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class SettingsController extends ABasicController {

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private SettingsMapper settingsMapper;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SettingsAdminDto>> getList(SettingsCriteria settingsCriteria, Pageable pageable) {
        
        if (!isAdmin()) {
            throw new UnauthorizationException("Not allowed to get");
        }
        ApiMessageDto<ResponseListObj<SettingsAdminDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Settings> settingsPage = settingsRepository.findAll(settingsCriteria.getSpecification(),pageable);
        ResponseListObj<SettingsAdminDto> responseListObj = new ResponseListObj<>();

        responseListObj.setData(settingsMapper.fromEntityListToAdminDtoList(settingsPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(settingsPage.getTotalPages());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List setting success");
        
        return apiMessageDto;
    }

    @GetMapping(value = "/client-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<SettingsDto>> getClientList(SettingsCriteria settingsCriteria, Pageable pageable) {
        ApiMessageDto<ResponseListObj<SettingsDto>> apiMessageDto = new ApiMessageDto<>();
        Page<Settings> settingsPage = settingsRepository.findAll(settingsCriteria.getSpecification(),pageable);
        ResponseListObj<SettingsDto> responseListObj = new ResponseListObj<>();

        responseListObj.setData(settingsMapper.fromEntityListToDtoList(settingsPage.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(settingsPage.getTotalPages());

        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("List setting success");

        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<SettingsAdminDto> get(@PathVariable("id") Long id) {
        
        if (!isAdmin()) {
            throw new UnauthorizationException("Not allowed to get.");
        }
        ApiMessageDto<SettingsAdminDto> apiMessageDto = new ApiMessageDto<>();
        
        Settings settings = settingsRepository.findById(id).orElse(null);
        // check null
        if (settings == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Can not get settings");
            return apiMessageDto;
        }
        apiMessageDto.setData(settingsMapper.fromEntityToAdminDto(settings));
        apiMessageDto.setMessage("Get settings success");
        return apiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateSettingsForm createSettingsForm,
            BindingResult bindingResult) {

        if (!isAdmin()) {
            throw new UnauthorizationException("Not allowed to create");
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Settings check = settingsRepository.findSettingsByKey(createSettingsForm.getSettingKey());
        if(check != null){
            apiMessageDto.setResult(false);
            apiMessageDto.setCode("ERROR-KEY-EXISTS");
            return apiMessageDto;
        }

        Settings settings = settingsMapper.fromCreateSettingsToSetting(createSettingsForm);
        settingsRepository.save(settings);
        apiMessageDto.setMessage("Create setting success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateSettingsForm updateSettingsForm,
            BindingResult bindingResult) {

        if (!isAdmin()) {
            throw new UnauthorizationException("Not allowed to update");
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Settings settings = settingsRepository.findById(updateSettingsForm.getId()).orElse(null);

        if (settings == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Settings does not exist to update");
            return apiMessageDto;
        }
        if(!settings.isEditable()){
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Can not edit this settings");
            return apiMessageDto;
        }
        settingsMapper.fromUpdateSettingsToSetting(updateSettingsForm, settings);
        settingsRepository.save(settings);
        apiMessageDto.setMessage("Update settings success");
        return apiMessageDto;
    }
    
    @Transactional
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable Long id) {

        if (!isAdmin()) {
            throw new UnauthorizationException("Not allowed to delete");
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Settings settings = settingsRepository.findById(id).orElse(null);
        if (settings == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setData("Settings does not exist to delete");
            return apiMessageDto;
        }
        settingsRepository.deleteById(id);
        apiMessageDto.setMessage("Delete settings success");
        return apiMessageDto;
    }
}
