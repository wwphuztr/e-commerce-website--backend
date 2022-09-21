package com.nails.api.dto.category;

import com.nails.api.dto.ABasicAdminDto;
import com.nails.api.dto.product.ProductAdminDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductsByCategoryDto extends ABasicAdminDto {
    private String categoryName;
    private List<ProductAdminDto> productList;
}
