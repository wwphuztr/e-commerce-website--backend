package com.nails.api.controller;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.nails.api.constant.NailsConstant;
import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ErrorCode;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.customer.CustomerDto;
import com.nails.api.dto.customer.VerifyNewCustomerDto;
import com.nails.api.exception.NotFoundException;
import com.nails.api.exception.RequestException;
import com.nails.api.exception.UnauthorizationException;
import com.nails.api.form.account.CreateAccountAdminForm;
import com.nails.api.form.account.UpdateAccountAdminForm;
import com.nails.api.form.customer.CreateCustomerForm;
import com.nails.api.form.customer.CustomerRegisterForm;
import com.nails.api.form.customer.UpdateCustomerAdminForm;
import com.nails.api.form.customer.UpdateCustomerProfileForm;
import com.nails.api.mapper.CustomerMapper;
import com.nails.api.service.NailsApiService;
import com.nails.api.storage.criteria.CustomerCriteria;
import com.nails.api.storage.model.*;
import com.nails.api.storage.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CustomerController extends ABasicController {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    NailsApiService nailsApiService;

    @Autowired
    ProvinceRepository provinceRepository;

    @Autowired
    AddressesRepository addressesRepository;

    static final String NOT_ALLOW_GET_MESSAGE = "Not allowed get.";

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerDto>> list(CustomerCriteria customerCriteria, Pageable pageable) {
        if(!isAdmin()) {
            throw new UnauthorizationException(NOT_ALLOW_GET_MESSAGE);
        }
        ApiMessageDto<ResponseListObj<CustomerDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Customer> listCustomer = customerRepository.findAll(customerCriteria.getSpecification(), pageable);
        ResponseListObj<CustomerDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(customerMapper.fromEntityListToAdminDtoList(listCustomer.getContent()));
        responseListObj.setTotalPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCustomer.getTotalPages());
        responseListObj.setTotalElements(listCustomer.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<CustomerDto>> autocomplete(CustomerCriteria customerCriteria, Pageable pageable){
        if(!isAdmin()) {
            throw new UnauthorizationException(NOT_ALLOW_GET_MESSAGE);
        }
        ApiMessageDto<ResponseListObj<CustomerDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Customer> listCustomer = customerRepository.findAll(customerCriteria.getSpecification(), pageable);
        ResponseListObj<CustomerDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(customerMapper.fromEntityListToAdminDtoListAutocomplete(listCustomer.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(listCustomer.getTotalPages());
        responseListObj.setTotalElements(listCustomer.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerDto> get(@PathVariable("id") Long id) {
        if(!isAdmin()) {
            throw new UnauthorizationException(NOT_ALLOW_GET_MESSAGE);
        }
        ApiMessageDto<CustomerDto> result = new ApiMessageDto<>();
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new NotFoundException("Not found customer");
        }
        result.setData(customerMapper.fromEntityToAdminDto(customer));
        result.setMessage("Get customer success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateCustomerForm createCustomerForm, BindingResult bindingResult) {
        if(!isAdmin()) {
            throw new UnauthorizationException("Not allowed create customer");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        Long checkAccount = accountRepository.countAccountByPhone(createCustomerForm.getCustomerPhone());
        if(checkAccount > 0) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Customer phone is exist");
            return apiMessageDto;
        }

        Group group = groupRepository.findFirstByKind(NailsConstant.GROUP_KIND_CUSTOMER);
        if(group == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Group does not exists!");
            return apiMessageDto;
        }

        Customer customer = customerMapper.fromCreateCustomerFormToEntity(createCustomerForm);
        customer.getAccount().setGroup(group);
        customer.getAccount().setPassword(passwordEncoder.encode(createCustomerForm.getCustomerPassword()));
        customer.getAccount().setKind(NailsConstant.USER_KIND_CUSTOMER);
        customer.getAccount().setStatus(createCustomerForm.getStatus());

        if(createCustomerForm.getIsLoyalty()) {
            customer.setLoyaltyDate(new Date());
        }

        customerRepository.save(customer);
        apiMessageDto.setMessage("Create account customer success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateCustomerAdminForm updateCustomerAdminForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new UnauthorizationException("Not allowed update.");
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Customer customer = customerRepository.findById(updateCustomerAdminForm.getId()).orElse(null);
        if (customer == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Customer not found");
            return apiMessageDto;
        }

        if(customer.getIsLoyalty().equals(Boolean.FALSE) && updateCustomerAdminForm.getIsLoyalty().equals(Boolean.TRUE)) {
            customer.setLoyaltyDate(new Date());
        }

        customerMapper.fromUpdateCustomerAdminFromToEntity(updateCustomerAdminForm, customer);

        if(updateCustomerAdminForm.getIsLoyalty().equals(Boolean.FALSE)) {
            customer.setLoyaltyDate(null);
            customer.setLoyaltyLevel(null);
            customer.setSaleOff(0);
        }

        if (StringUtils.isNoneBlank(updateCustomerAdminForm.getCustomerPassword())) {
            customer.getAccount().setPassword(passwordEncoder.encode(updateCustomerAdminForm.getCustomerPassword()));
        }

        if (StringUtils.isNoneBlank(updateCustomerAdminForm.getCustomerAvatarPath())) {
            if(!updateCustomerAdminForm.getCustomerAvatarPath().equals(customer.getAccount().getAvatarPath())){
                //delete old image
                nailsApiService.deleteFile(customer.getAccount().getAvatarPath());
            }
            customer.getAccount().setAvatarPath(updateCustomerAdminForm.getCustomerAvatarPath());
        }

        customer.getAccount().setStatus(updateCustomerAdminForm.getStatus());
        accountRepository.save(customer.getAccount());
        customerRepository.save(customer);

        apiMessageDto.setMessage("Update customer success");
        return apiMessageDto;

    }

    @DeleteMapping(value = "/delete/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        if(!isAdmin()) {
            throw new UnauthorizationException("Not allowed delete");
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new NotFoundException("Not found customer");
        }
        nailsApiService.deleteFile(customer.getAccount().getAvatarPath());
        customerRepository.delete(customer);
        result.setMessage("Delete list success");
        return result;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<VerifyNewCustomerDto> register(@Valid @RequestBody CustomerRegisterForm customerRegisterForm, BindingResult bindingResult) {
        ApiMessageDto<VerifyNewCustomerDto> apiMessageDto = new ApiMessageDto<>();

        Long checkCustomer = customerRepository
                .countBy(customerRegisterForm.getCustomerPhone());
        if (checkCustomer > 0) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_PHONE_EXIST);
        }

        Province province = provinceRepository.findById(customerRegisterForm.getProvinceId()).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND_PROVINCE);
        }
        Province district = provinceRepository.findById(customerRegisterForm.getDistrictId()).orElse(null);
        if(district == null || !district.getParentProvince().getId().equals(province.getId())) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND_PROVINCE);
        }
        Province commune = provinceRepository.findById(customerRegisterForm.getCommuneId()).orElse(null);
        if(commune == null || !commune.getParentProvince().getId().equals(district.getId())) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND_PROVINCE);
        }

        Group group = groupRepository.findFirstByKind(NailsConstant.GROUP_KIND_CUSTOMER);
        if (group == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_GROUP_NOT_EXIST);
        }

        Customer customer = customerMapper.fromCustomerRegisterFormToEntity(customerRegisterForm);

        customer.getAccount().setGroup(group);
        customer.getAccount().setPassword(passwordEncoder.encode(customerRegisterForm.getCustomerPassword()));
        customer.getAccount().setKind(NailsConstant.USER_KIND_CUSTOMER);

        Addresses addresses = new Addresses();
        addresses.setAddress(customerRegisterForm.getAddress());
        addresses.setCustomer(customer);
        addresses.setCommune(commune);
        addresses.setDistrict(district);
        addresses.setProvince(province);
        addresses.setPhone(customer.getAccount().getPhone());
        addresses.setName(customer.getAccount().getFullName());

        customerRepository.save(customer);
        addressesRepository.save(addresses);
        apiMessageDto.setMessage("Register success");
        return apiMessageDto;
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CustomerDto> profile() {
        long id = getCurrentUserId();
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
        }
        ApiMessageDto<CustomerDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(customerMapper.fromEntityToClientProfileDto(customer));
        apiMessageDto.setMessage("Get profile success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update_profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfile(@Valid @RequestBody UpdateCustomerProfileForm updateCustomerProfileForm, BindingResult bindingResult) {

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        long id = getCurrentUserId();
        Customer customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_NOT_FOUND);
        }
        Account account = customer.getAccount();

        if(!updateCustomerProfileForm.getCustomerEmail().equals(account.getEmail())) {
            Long checkEmailExist = accountRepository.countAccountByEmail(updateCustomerProfileForm.getCustomerEmail());
            if(checkEmailExist > 0) {
                throw new RequestException(ErrorCode.CUSTOMER_ERROR_EMAIL_EXIST);
            }
        }

        if(!passwordEncoder.matches(updateCustomerProfileForm.getOldPassword(), account.getPassword())){
            throw new RequestException(ErrorCode.CUSTOMER_ERROR_OLD_PWD_NOT_MATCH);
        }

        if (StringUtils.isNoneBlank(updateCustomerProfileForm.getCustomerPassword())) {
            account.setPassword(passwordEncoder.encode(updateCustomerProfileForm.getCustomerPassword()));
        }

        if (StringUtils.isNoneBlank(updateCustomerProfileForm.getCustomerAvatarPath())) {
            if(!updateCustomerProfileForm.getCustomerAvatarPath().equals(account.getAvatarPath())){
                //delete old image
                nailsApiService.deleteFile(account.getAvatarPath());
            }
            account.setAvatarPath(updateCustomerProfileForm.getCustomerAvatarPath());
        }

        customerMapper.fromUpdateCustomerProfileFormToEntity(updateCustomerProfileForm, customer);
        customerRepository.save(customer);
        accountRepository.save(customer.getAccount());

        apiMessageDto.setMessage("Update customer profile success");
        return apiMessageDto;
    }
}
