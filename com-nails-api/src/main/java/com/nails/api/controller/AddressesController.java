package com.nails.api.controller;

import com.nails.api.constant.NailsConstant;
import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ErrorCode;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.addresses.AddressesDto;
import com.nails.api.exception.RequestException;
import com.nails.api.form.addresses.CreateAddressesForm;
import com.nails.api.form.addresses.UpdateAddressesForm;
import com.nails.api.mapper.AddressesMapper;
import com.nails.api.storage.criteria.AddressesCriteria;
import com.nails.api.storage.model.Account;
import com.nails.api.storage.model.Addresses;
import com.nails.api.storage.model.Customer;
import com.nails.api.storage.model.Province;
import com.nails.api.storage.repository.AccountRepository;
import com.nails.api.storage.repository.AddressesRepository;
import com.nails.api.storage.repository.CustomerRepository;
import com.nails.api.storage.repository.ProvinceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/addresses")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AddressesController extends ABasicController{
    @Autowired
    AddressesRepository addressesRepository;

    @Autowired
    AddressesMapper addressesMapper;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ProvinceRepository provinceRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AddressesDto>> list(AddressesCriteria addressesCriteria, Pageable pageable) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ADDRESS_ERROR_UNAUTHORIZED, "Not allowed get list");
        }
        ApiMessageDto<ResponseListObj<AddressesDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Addresses> list = addressesRepository.findAll(addressesCriteria.getSpecification(),pageable);
        ResponseListObj<AddressesDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(addressesMapper.fromEntityListToAddressesDtoList(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<AddressesDto>> listAutoComplete(AddressesCriteria addressesCriteria) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)){
            throw new RequestException(ErrorCode.ADDRESS_ERROR_UNAUTHORIZED, "Not allowed get auto complete");
        }
        ApiMessageDto<ResponseListObj<AddressesDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        if(currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)) {
            addressesCriteria.setCustomerId(currentUser.getId());
        }

        if(addressesCriteria.getCustomerId() == null) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_BAD_REQUEST, "Not allowed get auto complete");
        }

        List<Addresses> list = addressesRepository.findAll(addressesCriteria.getSpecification());
        ResponseListObj<AddressesDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(addressesMapper.fromEntityListToAdminDtoListAutoComplete(list));

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AddressesDto> get(@PathVariable("id") Long id) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)){
            throw new RequestException(ErrorCode.ADDRESS_ERROR_UNAUTHORIZED, "Not allowed to get");
        }

        ApiMessageDto<AddressesDto> result = new ApiMessageDto<>();
        Addresses addresses = addressesRepository.findById(id).orElse(null);
        if(addresses == null){
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Address not found.");
        }
        // check owner for customer role
        if(currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)
                && !currentUser.getId().equals(addresses.getCustomer().getId())) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Address not found");
        }
        result.setData(addressesMapper.fromEntityToAddressesDto(addresses));
        result.setMessage("Get address success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateAddressesForm createAddressesForm, BindingResult bindingResult) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allowed to create");
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        if(currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)) {
            createAddressesForm.setCustomerId(currentUser.getId());
        }

        Customer customer = customerRepository.findById(createAddressesForm.getCustomerId()).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Address not found");
        }

        Province province = provinceRepository.findById(createAddressesForm.getProvinceId()).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Province not found");
        }
        Province district = provinceRepository.findById(createAddressesForm.getDistrictId()).orElse(null);
        if(district == null || !district.getParentProvince().getId().equals(province.getId())) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "District not found");
        }
        Province commune = provinceRepository.findById(createAddressesForm.getCommuneId()).orElse(null);
        if(commune == null || !commune.getParentProvince().getId().equals(district.getId())) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Commune not found");
        }
        Addresses addresses = addressesMapper.fromCreateAddressesFormToEntity(createAddressesForm);

        addressesRepository.save(addresses);
        apiMessageDto.setMessage("Create address success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateAddressesForm updateAddressesForm, BindingResult bindingResult) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)){
            throw new RequestException(ErrorCode.GENERAL_ERROR_UNAUTHORIZED, "Not allowed to update");
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        Addresses addresses = addressesRepository.findById(updateAddressesForm.getId()).orElse(null);
        if (addresses == null) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Address not found.");
        }
        Province province = provinceRepository.findById(updateAddressesForm.getProvinceId()).orElse(null);
        if(province == null) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Province not found");
        }
        Province district = provinceRepository.findById(updateAddressesForm.getDistrictId()).orElse(null);
        if(district == null || !district.getParentProvince().getId().equals(province.getId())) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "District not found");
        }
        Province commune = provinceRepository.findById(updateAddressesForm.getCommuneId()).orElse(null);
        if(commune == null || !commune.getParentProvince().getId().equals(district.getId())) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Commune not found");
        }

        // Check owner for customer role
        if(currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)
                && !addresses.getCustomer().getId().equals(currentUser.getId())) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Not found address");
        }

        addressesMapper.fromUpdateAddressesFormToEntity(updateAddressesForm, addresses);

        addressesRepository.save(addresses);

        apiMessageDto.setMessage("Update address success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id){
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)
                && !currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)){
            throw new RequestException(ErrorCode.ADDRESS_ERROR_UNAUTHORIZED, "Not allowed to delete");
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();

        Addresses addresses = addressesRepository.findById(id).orElse(null);
        if(addresses == null){
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Not found address");
        }

        if(currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)
                && !currentUser.getId().equals(addresses.getCustomer().getId())) {
            throw new RequestException(ErrorCode.ADDRESS_ERROR_NOT_FOUND, "Not found addresses");
        }

        addressesRepository.delete(addresses);
        result.setMessage("Delete address success");
        return result;
    }
}
