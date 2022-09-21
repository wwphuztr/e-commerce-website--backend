package com.nails.api.controller;

import com.nails.api.constant.NailsConstant;
import com.nails.api.dto.ApiMessageDto;
import com.nails.api.dto.ErrorCode;
import com.nails.api.dto.ResponseListObj;
import com.nails.api.dto.news.NewsDto;
import com.nails.api.exception.NotFoundException;
import com.nails.api.exception.RequestException;
import com.nails.api.exception.UnauthorizationException;
import com.nails.api.form.news.CreateNewsForm;
import com.nails.api.form.news.UpdateNewsForm;
import com.nails.api.mapper.NewsMapper;
import com.nails.api.service.NailsApiService;
import com.nails.api.storage.criteria.NewsCriteria;
import com.nails.api.storage.model.Account;
import com.nails.api.storage.model.News;
import com.nails.api.storage.repository.AccountRepository;
import com.nails.api.storage.repository.NewsRepository;
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
@RequestMapping("/v1/news")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NewsController extends ABasicController{
    @Autowired
    NewsRepository newsRepository;

    @Autowired
    NewsMapper newsMapper;

    @Autowired
    NailsApiService nailsApiService;

    @Autowired
    AccountRepository accountRepository;

    private static final String NOT_ALLOWED = "Not allowed";

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<NewsDto>> list(NewsCriteria newsCriteria, Pageable pageable) {
        if(!isAdmin()) {
            throw new UnauthorizationException(NOT_ALLOWED);
        }
        ApiMessageDto<ResponseListObj<NewsDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<News> list = newsRepository.findAll(newsCriteria.getSpecification(), pageable);
        ResponseListObj<NewsDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(newsMapper.fromEntityListToNewsDtoListNoNewsContent(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-list-news", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListObj<NewsDto>> listforClient(NewsCriteria newsCriteria, Pageable pageable) {

        ApiMessageDto<ResponseListObj<NewsDto>> responseListObjApiMessageDto = new ApiMessageDto<>();

        Page<News> list = newsRepository.findAll(newsCriteria.getSpecification(), pageable);
        ResponseListObj<NewsDto> responseListObj = new ResponseListObj<>();
        responseListObj.setData(newsMapper.fromEntityListToNewsDtoListNoNewsContent(list.getContent()));
        responseListObj.setPage(pageable.getPageNumber());
        responseListObj.setTotalPage(list.getTotalPages());
        responseListObj.setTotalElements(list.getTotalElements());

        responseListObjApiMessageDto.setData(responseListObj);
        responseListObjApiMessageDto.setMessage("Get list success");
        return responseListObjApiMessageDto;
    }

    @GetMapping(value = "/client-news-detail/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<NewsDto> getforClient(@PathVariable("id") Long id){
        ApiMessageDto<NewsDto> result = new ApiMessageDto<>();
        News news = newsRepository.findById(id).orElse(null);
        if(news == null){
            throw new RequestException(ErrorCode.NEWS_ERROR_NOT_FOUND);
        }
        if(!news.getStatus().equals(NailsConstant.STATUS_ACTIVE)) {
            throw new RequestException(ErrorCode.NEWS_ERROR_NOT_FOUND);
        }

        result.setData(newsMapper.fromEntityToNewsDto(news));
        result.setMessage("Get news success");
        return result;
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<NewsDto> get(@PathVariable("id") Long id) {
        Account currentUser = accountRepository.findById(getCurrentUserId()) .orElse(null);
        if(currentUser == null
                || !currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)) {
            throw new UnauthorizationException(NOT_ALLOWED);
        }

        ApiMessageDto<NewsDto> result = new ApiMessageDto<>();
        News news = newsRepository.findById(id).orElse(null);
        if(news == null){
            throw new NotFoundException("Not found news");
        }
        if(!currentUser.getKind().equals(NailsConstant.USER_KIND_ADMIN)
                && !news.getStatus().equals(NailsConstant.STATUS_ACTIVE)) {
            throw new NotFoundException("Not found news");
        }

        result.setData(newsMapper.fromEntityToNewsDto(news));
        result.setMessage("Get news success");
        return result;
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> create(@Valid @RequestBody CreateNewsForm createNewsForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new UnauthorizationException(NOT_ALLOWED);
        }
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        News news = newsMapper.fromCreateNewsFormToEntity(createNewsForm);

        newsRepository.save(news);
        apiMessageDto.setMessage("Create news success");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateNewsForm updateNewsForm, BindingResult bindingResult) {
        if(!isAdmin()){
            throw new UnauthorizationException(NOT_ALLOWED);
        }

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        News news = newsRepository.findById(updateNewsForm.getId()).orElse(null);
        if (news == null) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("News not found");
            return apiMessageDto;
        }

        newsMapper.fromUpdateNewsFormToEntity(updateNewsForm, news);

        if (StringUtils.isNoneBlank(updateNewsForm.getAvatar())) {
            if(!updateNewsForm.getAvatar().equals(news.getAvatar())){
                //delete old image
                nailsApiService.deleteFile(news.getAvatar());
            }
            news.setAvatar(updateNewsForm.getAvatar());
        }

        newsRepository.save(news);

        apiMessageDto.setMessage("Update news success");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        if(!isAdmin()){
            throw new UnauthorizationException(NOT_ALLOWED);
        }
        ApiMessageDto<String> result = new ApiMessageDto<>();
        News news = newsRepository.findById(id).orElse(null);
        if(news == null){
            throw new NotFoundException("News not found");
        }
        nailsApiService.deleteFile(news.getAvatar());
        newsRepository.delete(news);
        result.setMessage("Delete success");
        return result;
    }
}
