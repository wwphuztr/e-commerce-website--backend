package com.nails.api.mapper;

import com.nails.api.dto.product.ProductAdminDto;
import com.nails.api.form.product.CreateProductAdminForm;
import com.nails.api.form.product.UpdateProductAdminForm;
import com.nails.api.storage.model.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {

    @Mapping(source = "productName", target = "name")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "productImage", target = "image")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "labelColor", target = "labelColor")
    @Mapping(source = "saleoff", target = "saleoff")
    //@Mapping(source = "quantity", target = "quantity")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminCreateMapping")
    Product fromCreateProductAdminFormToEntity( CreateProductAdminForm createProductAdminForm );

    @Mapping(source = "productName", target = "name")
    @Mapping(source = "productPrice", target = "price")
    @Mapping(source = "productImage", target = "image")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "labelColor", target = "labelColor")
    @Mapping(source = "saleoff", target = "saleoff")
    //@Mapping(source = "quantity", target = "quantity")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminUpdateMapping")
    void fromUpdateProductAdminFormToEntity(UpdateProductAdminForm updateProductAdminForm, @MappingTarget Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "image", target = "productImage")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "labelColor", target = "labelColor")
    @Mapping(source = "saleoff", target = "saleoff")
    @Mapping(source = "parentProduct.id", target = "parentId")
    //@Mapping(source = "quantity", target = "quantity")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetMapping")
    ProductAdminDto fromEntityToAdminDto(Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "image", target = "productImage")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "shortDescription", target = "shortDescription")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedBy", target = "modifiedBy")
    @Mapping(source = "createdBy", target = "createdBy")
    @Mapping(source = "labelColor", target = "labelColor")
    @Mapping(source = "saleoff", target = "saleoff")
    @Mapping(source = "parentProduct.id", target = "parentId")
    //@Mapping(source = "quantity", target = "quantity")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminClientGetMapping")
    ProductAdminDto fromEntityToAdminDtoforClient(Product product);

    @IterableMapping(elementTargetType = ProductAdminDto.class, qualifiedByName = "adminGetMapping")
    List<ProductAdminDto> fromEntityListToProductDtoList(List<Product> productList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "image", target = "productImage")
    @Mapping(source = "labelColor", target = "labelColor")
    @Mapping(source = "saleoff", target = "saleoff")
    @Mapping(source = "productList", target = "productChilds", qualifiedByName = "adminGetAutoCompleteListMapping")
    @Mapping(source = "parentProduct.name", target = "parentName")
    @Mapping(source = "hasChild", target = "hasChild")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminGetAutoCompleteMapping")
    ProductAdminDto fromEntityToAdminDtoAutoComplete(Product product);

    @IterableMapping(elementTargetType = ProductAdminDto.class, qualifiedByName = "adminGetAutoCompleteMapping")
    @Named("adminGetAutoCompleteListMapping")
    List<ProductAdminDto> fromEntityListToProductDtoListAutoComplete(List<Product> productList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "productName")
    @Mapping(source = "price", target = "productPrice")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "image", target = "productImage")
    @Mapping(source = "hasChild", target = "hasChild")
    @Mapping(source = "labelColor", target = "labelColor")
    @Mapping(source = "saleoff", target = "saleoff")
    @Mapping(source = "parentProduct.id", target = "parentId")
    @BeanMapping(ignoreByDefault = true)
    @Named("clientGetMapping")
    ProductAdminDto fromEntityToClientDto(Product product);

    @IterableMapping(elementTargetType = ProductAdminDto.class, qualifiedByName = "clientGetMapping")
    List<ProductAdminDto> fromEntityListToProductDtoListclient(List<Product> productList);
}
