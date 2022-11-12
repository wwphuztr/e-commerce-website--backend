package com.nails.api.controller;

import com.nails.api.constant.NailsConstant;
import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.category.ProductsByCategoryDto;
import com.nails.api.dto.product.ProductAdminDto;
import com.nails.api.exception.NotFoundException;
import com.nails.api.exception.UnauthorizationException;
import com.nails.api.form.customer.CreateCustomerForm;
import com.nails.api.form.product.CreateProductAdminForm;
import com.nails.api.form.product.UpdateProductAdminForm;
import com.nails.api.mapper.CategoryMapper;
import com.nails.api.mapper.ProductMapper;
import com.nails.api.service.NailsApiService;
import com.nails.api.storage.criteria.CategoryCriteria;
import com.nails.api.storage.criteria.ProductCriteria;
import com.nails.api.storage.model.Account;
import com.nails.api.storage.model.Category;
import com.nails.api.storage.model.Product;
import com.nails.api.storage.repository.AccountRepository;
import com.nails.api.storage.repository.CategoryRepository;
import com.nails.api.storage.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProductController extends ABasicController{
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    NailsApiService nailsApiService;

    @Autowired
    AccountRepository accountRepository;

    static final String GET_LIST_SUCCESS_MESSAGE = "Get list success";
    static final String NOT_FOUND_PRODUCT = "Not found product";
    static final String NOT_ALLOW_GET_LIST = "Not allowed to get list";

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductAdminDto>> list(ProductCriteria productCriteria, Pageable pageable) {
        if(!isAdmin()){
            throw new UnauthorizationException(NOT_ALLOW_GET_LIST);
        }
        ApiMessageDto<ResponseListObj<ProductAdminDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Product> list = productRepository.findAll(productCriteria.getSpecification(), pageable);
        ResponseListObj<ProductAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoList(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage(GET_LIST_SUCCESS_MESSAGE);
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductAdminDto>> clientlist(ProductCriteria productCriteria, Pageable pageable) {
        ApiMessageDto<ResponseListObj<ProductAdminDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Product> list = productRepository.findAll(productCriteria.getSpecification(), pageable);
        ResponseListObj<ProductAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoListclient(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage(GET_LIST_SUCCESS_MESSAGE);
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-list-topsales", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductAdminDto>> clientlist_topsales(ProductCriteria productCriteria, Pageable pageable){
        ApiMessageDto<ResponseListObj<ProductAdminDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        //List<Product> list = productRepository.topsales(pageable);
        List<Product> list = productRepository.findByOrderByPurchaseCountDesc(pageable);

        ResponseListObj<ProductAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoListclient(list));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage(GET_LIST_SUCCESS_MESSAGE);
        return responseListObjApiMessageDto;
    }

    @GetMapping(value ="/products-by-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductsByCategoryDto>> productsByCategory(CategoryCriteria categoryCriteria, Pageable pageable) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null) {
            throw new UnauthorizationException(NOT_ALLOW_GET_LIST);
        }
        if(!currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)) {
            throw new UnauthorizationException(NOT_ALLOW_GET_LIST);
        }

        ApiMessageDto<ResponseListObj<ProductsByCategoryDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        categoryCriteria.setKind(NailsConstant.CATEGORY_KIND_PRODUCT);
        categoryCriteria.setStatus(NailsConstant.STATUS_ACTIVE);
        Page<Category> listCategory = categoryRepository.findAll(categoryCriteria.getSpecification(), pageable);
        ResponseListObj<ProductsByCategoryDto> responseListObj = new ResponseListObj<>();

        List<ProductsByCategoryDto> listToProductsByCategoryDtoList = categoryMapper.fromEntityListToProductByCategoryDtoList(listCategory.getContent());

        responseListObj.setData(listToProductsByCategoryDtoList);
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCategory.getTotalPages());
        responseListObj.setTotalElements(listCategory.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage(GET_LIST_SUCCESS_MESSAGE);
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<ProductAdminDto>> autocomplete(ProductCriteria productCriteria) {
        ApiMessageDto<ResponseListObj<ProductAdminDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Product> list = productRepository.findAll(productCriteria.getSpecification(), Pageable.unpaged());
        ResponseListObj<ProductAdminDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(productMapper.fromEntityListToProductDtoListAutoComplete(list.getContent()));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage(GET_LIST_SUCCESS_MESSAGE);
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductAdminDto> get(@PathVariable Long id) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null
            || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)
        ) {
            throw new UnauthorizationException("Not allowed create");
        }
        ApiMessageDto<ProductAdminDto> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new NotFoundException(NOT_FOUND_PRODUCT);
        }

        result.setData(productMapper.fromEntityToAdminDto(product));
        result.setMessage("Get product success");
        return result;
    }

    @GetMapping(value = "/client-get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ProductAdminDto> getforClient(@PathVariable Long id) {

        ApiMessageDto<ProductAdminDto> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new NotFoundException(NOT_FOUND_PRODUCT);
        }

        result.setData(productMapper.fromEntityToAdminDtoforClient(product));
        result.setMessage("Get product success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateProductAdminForm createProductAdminForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed create.");
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();

        Category category = categoryRepository.findById(createProductAdminForm.getCategoryId()).orElse(null);
        if(category == null) {
            throw new NotFoundException("Not found category.");
        }
        Product product = productMapper.fromCreateProductAdminFormToEntity(createProductAdminForm);
        Long parentId = createProductAdminForm.getParentId();
        if(parentId != null) {
            Product parentProduct = productRepository.findById(parentId).orElse(null);
            if(parentProduct == null || parentProduct.getParentProduct() != null) {
                throw new NotFoundException("Not found parent product id.");
            }
            // If it's a child, set its parent hasChild = true
            else {
                product.setParentProduct(parentProduct);
                parentProduct.setHasChild(true);
                productRepository.save(parentProduct);
            }
        }
        // set cái purchase count lúc nào được tạo ra cũng bằng 0
        product.setPurchaseCount(0);
        productRepository.save(product);

        result.setMessage("Create product success");
        return result;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateProductAdminForm updateProductAdminForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed update.");
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();
        Category category = categoryRepository.findById(updateProductAdminForm.getCategoryId()).orElse(null);
        if(category == null) {
            throw new NotFoundException("Not found category.");
        }
        Product product = productRepository.findById(updateProductAdminForm.getId()).orElse(null);
        if(product == null) {
            throw new NotFoundException("Not found product.");
        }
        if(StringUtils.isNoneBlank(updateProductAdminForm.getProductImage())
                && updateProductAdminForm.getProductImage().equals(product.getImage())) {
            nailsApiService.deleteFile(product.getImage());
        }
        productMapper.fromUpdateProductAdminFormToEntity(updateProductAdminForm, product);
        productRepository.save(product);
        result.setMessage("Update product success");
        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ApiMessageDto<String> delete(@PathVariable Long id) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed delete.");
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();

        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new NotFoundException(NOT_FOUND_PRODUCT);
        }
        nailsApiService.deleteFile(product.getImage());
        productRepository.delete(product);

        if(product.getParentProduct() != null && productRepository.countProductByParentProductId(product.getParentProduct().getId()) <= 0) {
            product.getParentProduct().setHasChild(false);
            productRepository.save(product.getParentProduct());
        }

        result.setMessage("Delete product success");
        return result;
    }
}
