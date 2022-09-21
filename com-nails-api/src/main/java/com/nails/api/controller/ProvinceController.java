package com.nails.api.controller;

import com.nails.api.constant.NailsConstant;
import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ErrorCode;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.province.ProvinceDto;
import com.nails.api.exception.RequestException;
import com.nails.api.exception.UnauthorizationException;
import com.nails.api.form.province.CreateProvinceForm;
import com.nails.api.form.province.UpdateProvinceForm;
import com.nails.api.mapper.ProvinceMapper;
import com.nails.api.storage.criteria.ProvinceCriteria;
import com.nails.api.storage.model.Province;
import com.nails.api.storage.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/province")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProvinceController extends ABasicController{
    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    ProvinceMapper provinceMapper;

    private static final String NOT_ALLOWED = "Not allowed";

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProvinceDto>> list(ProvinceCriteria provinceCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<ResponseListObj<ProvinceDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Province> list = provinceRepository.findAll(provinceCriteria.getSpecification(), pageable);
        ResponseListObj<ProvinceDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(provinceMapper.fromEntityListToProvinceDtoList(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProvinceDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ProvinceDto> result = new ApiMessageDto<>();

        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND);
        }
        result.setData(provinceMapper.fromEntityToProvinceDto(province));
        result.setMessage("Get province success");
        return result;
    }

    // list all province, distric and commune which still active
    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProvinceDto>> autoComplete(ProvinceCriteria provinceCriteria) {
        ApiMessageDto<ResponseListObj<ProvinceDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        provinceCriteria.setStatus(NailsConstant.STATUS_ACTIVE);
        Page<Province> list = provinceRepository.findAll(provinceCriteria.getSpecification(), Pageable.unpaged());
        ResponseListObj<ProvinceDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(provinceMapper.fromEntityListToProvinceDtoListAutoComplete(list.getContent()));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProvinceForm createProvinceForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Province province = provinceMapper.fromCreateProvinceFormToEntity(createProvinceForm);
        province.setKind(NailsConstant.PROVINCE_KIND_PROVINCE);

        if(createProvinceForm.getParentId() != null) {
            Province parentProvince = provinceRepository.findById(createProvinceForm.getParentId()).orElse(null);
            if(parentProvince == null) {
                throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND);
            }
            if(parentProvince.getKind().equals(NailsConstant.PROVINCE_KIND_PROVINCE)) {
                province.setKind(NailsConstant.PROVINCE_KIND_DISTRICT);
            } else if(parentProvince.getKind().equals(NailsConstant.PROVINCE_KIND_DISTRICT)) {
                province.setKind(NailsConstant.PROVINCE_KIND_COMMUNE);
            }
            province.setParentProvince(parentProvince);
        }
        provinceRepository.save(province);
        apiMessageDto.setMessage("Create province success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProvinceForm updateProvinceForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Province province = provinceRepository.findById(updateProvinceForm.getId()).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND);
        }
        provinceMapper.fromUpdateProvinceFormToEntity(updateProvinceForm, province);
        provinceRepository.save(province);
        apiMessageDto.setMessage("Update province success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<ProvinceDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.PROVINCE_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ProvinceDto> result = new ApiMessageDto<>();

        Province province = provinceRepository.findById(id).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.PROVINCE_ERROR_NOT_FOUND);
        }
        provinceRepository.delete(province);
        result.setMessage("Delete province success");
        return result;
    }
}
