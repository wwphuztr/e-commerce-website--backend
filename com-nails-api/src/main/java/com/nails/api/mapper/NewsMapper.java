package com.nails.api.mapper;

import com.nails.api.dto.category.CategoryDto;
import com.nails.api.dto.category.ProductsByCategoryDto;
import com.nails.api.dto.news.NewsDto;
import com.nails.api.form.category.CreateCategoryForm;
import com.nails.api.form.category.UpdateCategoryForm;
import com.nails.api.form.news.CreateNewsForm;
import com.nails.api.form.news.UpdateNewsForm;
import com.nails.api.storage.model.Category;
import com.nails.api.storage.model.News;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NewsMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "banner", target = "banner")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "pinTop", target = "pinTop")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMappingNoNewsContent")
    NewsDto fromEntityToNewsDtoNoNewsContent(News news);

    @IterableMapping(elementTargetType = NewsDto.class, qualifiedByName = "adminGetMappingNoNewsContent")
    List<NewsDto> fromEntityListToNewsDtoListNoNewsContent(List<News> news);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "banner", target = "banner")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "pinTop", target = "pinTop")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    NewsDto fromEntityToNewsDto(News news);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "banner", target = "banner")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "pinTop", target = "pinTop")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    News fromCreateNewsFormToEntity(CreateNewsForm createNewsForm);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "avatar", target = "avatar")
    @Mapping(source = "banner", target = "banner")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "pinTop", target = "pinTop")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateNewsFormToEntity(UpdateNewsForm updateNewsForm, @MappingTarget News news);
}
