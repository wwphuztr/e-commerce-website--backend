package com.nails.api.controller;

import com.nails.api.constant.NailsConstant;
import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.category.CategoryDto;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.exception.NotFoundException;
import com.nails.api.exception.UnauthorizationException;
import com.nails.api.form.category.CreateCategoryForm;
import com.nails.api.form.category.UpdateCategoryForm;
import com.nails.api.mapper.CategoryMapper;
import com.nails.api.service.NailsApiService;
import com.nails.api.storage.criteria.CategoryCriteria;
import com.nails.api.storage.model.Category;
import com.nails.api.storage.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController{

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    NailsApiService nailsApiService;

    static final String NOT_FOUND_CATEGORY_MESSAGE = "Not found category";

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CategoryDto>> list(CategoryCriteria categoryCriteria, Pageable pageable) {
        if (!isAdmin()) {
            throw new UnauthorizationException("Not allowed get list.");
        }
        ApiMessageDto<ResponseListObj<CategoryDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Category> listCategory = categoryRepository.findAll(categoryCriteria.getSpecification(), pageable);
        ResponseListObj<CategoryDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(categoryMapper.fromEntityListToCategoryDtoList(listCategory.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCategory.getTotalPages());
        responseListObj.setTotalElements(listCategory.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CategoryDto>> client_list(CategoryCriteria categoryCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<CategoryDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Category> listCategory = categoryRepository.findAll(categoryCriteria.getSpecification(), pageable);
        ResponseListObj<CategoryDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(categoryMapper.fromEntityListToCategoryDtoListClient(listCategory.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCategory.getTotalPages());
        responseListObj.setTotalElements(listCategory.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CategoryDto>> autoComplete(CategoryCriteria categoryCriteria) {
        ApiMessageDto<ResponseListObj<CategoryDto>> responseListObjApiMessageDto = new ApiMessageDto<>();
        categoryCriteria.setStatus(NailsConstant.STATUS_ACTIVE);
        Page<Category> listCategory = categoryRepository.findAll(categoryCriteria.getSpecification(), Pageable.unpaged());
        ResponseListObj<CategoryDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(categoryMapper.fromEntityListToCategoryDtoAutoComplete(listCategory.getContent()));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CategoryDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed get.");
        }
        ApiMessageDto<CategoryDto> result = new ApiMessageDto<>();

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw  new NotFoundException(NOT_FOUND_CATEGORY_MESSAGE);
        }
        result.setData(categoryMapper.fromEntityToAdminDto(category));
        result.setMessage("Get category success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed create.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Category category = categoryMapper.fromCreateCategoryFormToEntity(createCategoryForm);
        if(createCategoryForm.getParentId() != null) {
            Category parentCategory = categoryRepository.findById(createCategoryForm.getParentId()).orElse(null);
            if(parentCategory == null || parentCategory.getParentCategory() != null) {
                throw new NotFoundException("Not found parent category.");
            }
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);
        apiMessageDto.setMessage("Create category success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed update.");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Category category = categoryRepository.findById(updateCategoryForm.getId()).orElse(null);
        if(category == null) {
            throw new NotFoundException(NOT_FOUND_CATEGORY_MESSAGE);
        }
        if(category.getStatus().equals(updateCategoryForm.getStatus()) && category.getParentCategory() == null) {
            category.getCategoryList().forEach(child -> child.setStatus(updateCategoryForm.getStatus()));
            categoryRepository.saveAll(category.getCategoryList());
        }
        if(StringUtils.isNoneBlank(category.getImage()) && !updateCategoryForm.getCategoryImage().equals(category.getImage())) {
            nailsApiService.deleteFile(category.getImage());
        }
        categoryMapper.fromUpdateCategoryFormToEntity(updateCategoryForm, category);
        categoryRepository.save(category);
        apiMessageDto.setMessage("Update category success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<CategoryDto> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed delete.");
        }
        ApiMessageDto<CategoryDto> result = new ApiMessageDto<>();

        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) {
            throw new NotFoundException(NOT_FOUND_CATEGORY_MESSAGE);
        }
        nailsApiService.deleteFile(category.getImage());
        categoryRepository.delete(category);
        result.setMessage("Delete category success");
        return result;
    }
}
