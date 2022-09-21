package com.nails.api.dto.product;

import com.nails.api.dto.ABasicAdminDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductAdminDto extends ABasicAdminDto {
    private String productName;
    private Long productPrice;
    private Long categoryId;
    private String productImage;
    private String description;
    private String shortDescription;
    private String labelColor;
    private Long parentId;
    private String parentName;
    private Boolean hasChild;
    private Integer saleoff;
    private Integer quantity;
    private List<ProductAdminDto> productChilds;
}
