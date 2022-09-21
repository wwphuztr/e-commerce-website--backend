package com.nails.api.controller;

import com.nails.api.constant.NailsConstant;
import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ErrorCode;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.orders.OrdersDetailDto;
import com.nails.api.dto.orders.OrdersDto;
import com.nails.api.dto.orders.ResponseListObjOrders;
import com.nails.api.exception.RequestException;
import com.nails.api.form.orders.*;
import com.nails.api.mapper.OrdersDetailMapper;
import com.nails.api.mapper.OrdersMapper;
import com.nails.api.storage.criteria.OrdersCriteria;
import com.nails.api.storage.model.*;
import com.nails.api.storage.repository.*;
import com.nails.api.utils.DateUtils;
import com.nails.api.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrdersController extends ABasicController{

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    OrdersDetailRepository ordersDetailRepository;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    OrdersDetailMapper ordersDetailMapper;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SettingsRepository settingsRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDto>> list(OrdersCriteria ordersCriteria, Pageable pageable) {
        if(!isAdmin()){
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<ResponseListObj<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Orders> ordersList = ordersRepository.findAll(ordersCriteria.getSpecification(), pageable);
        ResponseListObj<OrdersDto> responseListObj = new ResponseListObj<>();

        responseListObj.setData(ordersMapper.fromEntityListToOrdersDtoList(ordersList.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(ordersList.getTotalPages());
        responseListObj.setTotalElements(ordersList.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/my-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObjOrders<OrdersDto>> myOrders(OrdersCriteria ordersCriteria, Pageable pageable) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }
        Long currentUserId = currentUser.getId();
        if(!currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN))
        {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<ResponseListObjOrders<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<Orders> ordersList = ordersRepository.findAll(ordersCriteria.getSpecification(), pageable);
        ResponseListObjOrders<OrdersDto> responseListObjOrders = new ResponseListObjOrders<>();

        responseListObjOrders.setData(ordersMapper.fromEntityListToOrdersDtoList(ordersList.getContent()));
        responseListObjOrders.setPage(pageable.getPageNumber());
        responseListObjOrders.setTotalPage(ordersList.getTotalPages());
        responseListObjOrders.setTotalElements(ordersList.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObjOrders);
        responseListObjApiMessageDto.setMessage("Get list success..");
        return responseListObjApiMessageDto;
    }

    // not test
    @GetMapping(value = "/client-list-orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<OrdersDto>> clientListOrders(OrdersCriteria ordersCriteria, Pageable pageable) {
        if(!isCustomer()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }
        ApiMessageDto<ResponseListObj<OrdersDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        ordersCriteria.setCustomerId(getCurrentUserId());

        Page<Orders> ordersList = ordersRepository.findAll(ordersCriteria.getSpecification(), pageable);
        ResponseListObj<OrdersDto> responseListObj = new ResponseListObj<>();

        responseListObj.setData(ordersMapper.fromEntityListToClientOrdersDtoList(ordersList.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(ordersList.getTotalPages());
        responseListObj.setTotalElements(ordersList.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success.");
        return responseListObjApiMessageDto;
    }

    //not test
    @GetMapping(value = "/client-get-orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDto> clientGetOrders(@PathVariable("id") Long id) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }
        if(!currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();

        Orders orders = ordersRepository.findByIdAndCustomerId(id, currentUser.getId());

        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND);
        }
        OrdersDto ordersDto = ordersMapper.fromEntityToClientDto(orders);
        List<OrdersDetail> ordersDetailList = ordersDetailRepository.findByOrdersId(ordersDto.getId());
        List<OrdersDetailDto> ordersDetailDtoList = ordersDetailMapper.fromEntityListToOrdersDetailDtoList(ordersDetailList);
        ordersDto.setOrdersDetailDtos(ordersDetailDtoList);
        result.setData(ordersDto);
        result.setMessage("Get orders success");
        return result;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<OrdersDto> get(@PathVariable("id") Long id) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }
        boolean isAdmin = currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN);
        if(!isAdmin) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();

        Orders orders = null;
        orders = ordersRepository.findById(id).orElse(null);


        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND);
        }
        OrdersDto ordersDto = ordersMapper.fromEntityToAdminDto(orders);
        List<OrdersDetail> ordersDetailList = ordersDetailRepository.findByOrdersId(ordersDto.getId());
        List<OrdersDetailDto> ordersDetailDtoList = ordersDetailMapper.fromEntityListToOrdersDetailDtoList(ordersDetailList);
        ordersDto.setOrdersDetailDtos(ordersDetailDtoList);
        result.setData(ordersDto);
        result.setMessage("Get orders success");
        return result;
    }
    private void setPriceListForOrderDetailList(List<OrdersDetail> ordersDetailList) {
        for (int i = 0; i < ordersDetailList.size(); i++) {
            OrdersDetail ordersDetail = ordersDetailList.get(i);
            Product product = productRepository.findById(ordersDetail.getProduct().getId()).orElse(null);
            if(product == null) {
                throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND_PRODUCT);
            }
            if(product.getSaleoff() == null) {
                ordersDetail.setPrice(product.getPrice()*1.0);
            }else {
                ordersDetail.setPrice(product.getPrice() - product.getPrice() * (product.getSaleoff() / 100.0));
            }

            ordersDetailList.set(i, ordersDetail);
        }
    }

    private Customer createNewCustomerIfPhoneNotOwned(String phone, String email, String fullName, String address) {
        Customer customer = customerRepository.findByAccountPhone(phone);

        if(customer == null) {
            Account account = new Account();
            account.setPhone(phone);
            account.setEmail(email);
            account.setFullName(fullName);
            Group group = groupRepository.findFirstByKind(NailsConstant.GROUP_KIND_CUSTOMER);

            account.setGroup(group);
            account.setKind(NailsConstant.USER_KIND_CUSTOMER);
            account.setStatus(NailsConstant.STATUS_ACTIVE);

            customer = new Customer();
            customer.setAddress(address);
            customer.setAccount(account);

            customerRepository.save(customer);
        }
        return customer;
    }

    private Customer createNewClientIfPhoneNotOwned(String phone, String fullName, String address) {
        Customer customer = customerRepository.findByAccountPhone(phone);

        if(customer == null) {
            Account account = new Account();
            account.setPhone(phone);
            account.setFullName(fullName);
            Group group = groupRepository.findFirstByKind(NailsConstant.GROUP_KIND_CUSTOMER);

            account.setGroup(group);
            account.setKind(NailsConstant.USER_KIND_CUSTOMER);
            account.setStatus(NailsConstant.STATUS_ACTIVE);

            customer = new Customer();
            customer.setAddress(address);
            customer.setAccount(account);

            customerRepository.save(customer);
        }
        return customer;
    }

    private Double calculateTotalPriceOrders(List<OrdersDetail> ordersDetailList, Integer orderSaleOff, Integer orderVat) {
        double totalPriceProducts = 0d;
        for (OrdersDetail ordersDetail : ordersDetailList) {
            totalPriceProducts += ordersDetail.getPrice() * ordersDetail.getAmount();
        }

        double priceAfterSaleOff = totalPriceProducts - (totalPriceProducts * (orderSaleOff / 100.0));

        return priceAfterSaleOff + (priceAfterSaleOff * (orderVat / 100.0));
    }

    private String generateCode() {
        String code = StringUtils.generateRandomString(8);
        code = code.replace("0", "A");
        code = code.replace("O", "Z");
        return code;
    }

    @PostMapping(value = "/client-create")
    @Transactional
    public ApiMessageDto<OrdersDto> clientCreate(@Valid @RequestBody CreateOrdersClientForm createOrdersClientForm, BindingResult bindingResult) {
        Account currentUser = accountRepository.findById(getCurrentUserId()) .orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }
        if(createOrdersClientForm.getPaymentMethod().equals(NailsConstant.CASH_ON_DELIVERY_PAYMENT_KIND)  && createOrdersClientForm.getPaymentMethod().equals(NailsConstant.CASH_ONLINE_PAYMENT_KIND)) {
                throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST);
        }
        Customer customer = customerRepository.findById(currentUser.getId()).orElse(null);
        if(customer == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST);
        }
        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();

        List<CreateOrdersDetailForm> createOrdersDetailFormList = createOrdersClientForm.getOrdersDetailDtos();
        List<OrdersDetail> ordersDetailList = ordersDetailMapper.fromCreateOrdersDetailAdminFormListToEntityList(createOrdersDetailFormList);
        Orders orders = ordersMapper.fromCreateOrdersClientFormToEntity(createOrdersClientForm);

        setPriceListForOrderDetailList(ordersDetailList);

        orders.setCustomer(customer);

        orders.setState(NailsConstant.ORDERS_STATE_CREATED);
        orders.setPrevState(NailsConstant.ORDERS_STATE_CREATED);
        orders.setVat(NailsConstant.VAT);
        orders.setSaleOff(customer.getSaleOff());

        orders.setTotalMoney(calculateTotalPriceOrders(ordersDetailList, orders.getSaleOff(), orders.getVat()));

        orders.setCode(generateCode());

        Orders createdOrders = ordersRepository.save(orders);

        for (int i = 0; i < ordersDetailList.size(); i++) {
            OrdersDetail ordersDetail = ordersDetailList.get(i);
            ordersDetail.setOrders(createdOrders);
            ordersDetailList.set(i, ordersDetail);
        }
        ordersDetailRepository.saveAll(ordersDetailList);

        OrdersDto ordersDto = ordersMapper.fromEntityToAdminDto(orders);

        result.setData(ordersDto);
        result.setMessage("Create orders success");
        return result;
    }

    @PutMapping(value = "/update-state")
    public ApiMessageDto<String> updateState(@Valid @RequestBody UpdateOrdersForm updateOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> result = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(updateOrdersForm.getId()).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND);
        }

        if(updateOrdersForm.getOrdersState() < orders.getState()
                || updateOrdersForm.getOrdersState().equals(NailsConstant.ORDERS_STATE_CANCEL)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_INVALID_STATE);
        }
        if(updateOrdersForm.getOrdersState() < NailsConstant.ORDERS_STATE_DONE) {
            orders.setPrevState(updateOrdersForm.getOrdersState());
        }

        ordersMapper.fromUpdateOrdersAdminFormToEntity(updateOrdersForm, orders);
        ordersRepository.save(orders);
        result.setMessage("Update orders status success");
        return result;
    }

    @PutMapping(value = "/cancel-orders")
    public ApiMessageDto<String> cancelOrders(@Valid @RequestBody UpdateOrdersForm updateOrdersForm, BindingResult bindingResult) {
        if(!isAdmin()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> result = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(updateOrdersForm.getId()).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND);
        }
        if(!updateOrdersForm.getOrdersState().equals(NailsConstant.ORDERS_STATE_CANCEL)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_INVALID_STATE);
        }
        if(updateOrdersForm.getOrdersState().equals(NailsConstant.ORDERS_STATE_DONE)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_STATE_DONE);
        }
        ordersMapper.fromUpdateOrdersAdminFormToEntity(updateOrdersForm, orders);
        ordersRepository.save(orders);
        result.setMessage("Cancel orders status success");
        return result;
    }

    @PutMapping(value = "/client-cancel-orders/{id}")
    public ApiMessageDto<String> clientCancelOrders(@PathVariable Long id) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_CUSTOMER)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> result = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(id).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND);
        }
        if(!orders.getState().equals(NailsConstant.ORDERS_STATE_CREATED)
                || !currentUser.getId().equals(orders.getCustomer().getId())) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        Settings limitCancelTimeSettings = settingsRepository.findSettingsByKey(NailsConstant.ORDERS_LIMIT_CANCEL_TIME);
        if(limitCancelTimeSettings == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_SETTINGS_NOT_FOUND);
        }

        Integer limitCancelTimeValue = null;
        try {
            limitCancelTimeValue = Integer.parseInt(limitCancelTimeSettings.getValue());
        } catch (Exception ex){
            throw new RequestException(ErrorCode.ORDERS_ERROR_SETTINGS_INVALID_VALUE);
        }
        boolean isInTimeAllowed = DateUtils.isInRangeXMinutesAgo(orders.getCreatedDate(), limitCancelTimeValue * 60);
        if(!isInTimeAllowed) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_CANCEL_TIME_NOT_ALLOWED);
        }
        orders.setState(NailsConstant.ORDERS_STATE_CANCEL);

        ordersRepository.save(orders);
        result.setMessage("Cancel orders status success");
        return result;
    }

    @PutMapping(value = "/update")
    @Transactional
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateInfoOrdersForm updateInfoOrdersForm, BindingResult bindingResult) {
        Account currentUser = accountRepository.findById(getCurrentUserId()).orElse(null);
        if(currentUser == null || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        ApiMessageDto<String> result = new ApiMessageDto<>();
        Orders orders = ordersRepository.findById(updateInfoOrdersForm.getId()).orElse(null);
        if(orders == null) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND);
        }

        List<OrdersDetail> ordersDetailList = ordersDetailRepository.findByOrdersId(orders.getId());
        List<UpdateOrdersDetailForm> updateOrdersDetailFormList = updateInfoOrdersForm.getOrdersDetailDtos();
        List<UpdateOrdersDetailForm> deletingOrdersDetailsList = updateInfoOrdersForm.getDeletingOrdersDetails();

        if(orders.getState() > NailsConstant.ORDERS_STATE_CREATED) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_UNAUTHORIZED);
        }

        if(updateOrdersDetailFormList.isEmpty() || ordersDetailList.size() != updateOrdersDetailFormList.size() + deletingOrdersDetailsList.size()) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST);
        }

        orders.setCustomer(createNewCustomerIfPhoneNotOwned(updateInfoOrdersForm.getCustomerPhone(), updateInfoOrdersForm.getCustomerEmail(), updateInfoOrdersForm.getCustomerFullName(), updateInfoOrdersForm.getOrdersAddress()));

        updateOrdersDetailFormList.forEach(updateOrdersDetailForm -> {
            OrdersDetail ordersDetail = ordersDetailRepository.findById(updateOrdersDetailForm.getId()).orElse(null);
            ordersDetailMapper.fromUpdateOrdersDetailAdminFormToEntity(updateOrdersDetailForm, ordersDetail);
            ordersDetailRepository.save(ordersDetail);
        });

        for(UpdateOrdersDetailForm deletingOrdersDetail : deletingOrdersDetailsList) {
            OrdersDetail ordersDetail = ordersDetailRepository.findById(deletingOrdersDetail.getId()).orElse(null);
            if(ordersDetail == null) {
                throw new RequestException(ErrorCode.ORDERS_ERROR_NOT_FOUND_ORDERS_DETAIL);
            }
            ordersDetailRepository.delete(ordersDetail);
        }

        ordersMapper.fromUpdateOrdersInfoAdminFormToEntity(updateInfoOrdersForm, orders);

        orders.setTotalMoney(calculateTotalPriceOrders(ordersDetailRepository.findByOrdersId(orders.getId()), orders.getSaleOff(), orders.getVat()));

        ordersRepository.save(orders);
        result.setMessage("Update orders customer info success");
        return result;
    }

    @PostMapping(value = "/un-auth-create-orders")
    @Transactional
    public ApiMessageDto<OrdersDto> unAuthclientCreate(@Valid @RequestBody CreateOrdersClientForm createOrdersClientForm, BindingResult bindingResult) {

        if(createOrdersClientForm.getPaymentMethod().equals(NailsConstant.CASH_ON_DELIVERY_PAYMENT_KIND)  && createOrdersClientForm.getPaymentMethod().equals(NailsConstant.CASH_ONLINE_PAYMENT_KIND)) {
            throw new RequestException(ErrorCode.ORDERS_ERROR_BAD_REQUEST);
        }

        ApiMessageDto<OrdersDto> result = new ApiMessageDto<>();

        List<CreateOrdersDetailForm> createOrdersDetailFormList = createOrdersClientForm.getOrdersDetailDtos();
        List<OrdersDetail> ordersDetailList = ordersDetailMapper.fromCreateOrdersDetailAdminFormListToEntityList(createOrdersDetailFormList);
        Orders orders = ordersMapper.fromCreateOrdersClientFormToEntity(createOrdersClientForm);
        setPriceListForOrderDetailList(ordersDetailList);

        Customer customer = customerRepository.findByAccountPhone(createOrdersClientForm.getReceiverPhone());
        if(customer == null) {
            orders.setCustomer(createNewClientIfPhoneNotOwned(createOrdersClientForm.getReceiverPhone(), createOrdersClientForm.getReceiverName(), createOrdersClientForm.getOrdersAddress()));
        }
        else {
            orders.setCustomer(customer);
        }

        orders.setState(NailsConstant.ORDERS_STATE_CREATED);
        orders.setPrevState(NailsConstant.ORDERS_STATE_CREATED);
        orders.setVat(NailsConstant.VAT);

        orders.setTotalMoney(calculateTotalPriceOrders(ordersDetailList, orders.getSaleOff(), orders.getVat()));

        orders.setCode(generateCode());

        Orders createdOrders = ordersRepository.save(orders);

        for (int i = 0; i < ordersDetailList.size(); i++) {
            OrdersDetail ordersDetail = ordersDetailList.get(i);
            ordersDetail.setOrders(createdOrders);
            ordersDetailList.set(i, ordersDetail);
        }
        ordersDetailRepository.saveAll(ordersDetailList);

        OrdersDto ordersDto = ordersMapper.fromEntityToAdminDto(orders);

        result.setData(ordersDto);
        result.setMessage("Create orders success");
        return result;
    }
}
