package com.nails.api.dto.news;

import com.nails.api.dto.ABasicAdminDto;
import lombok.Data;

@Data
public class NewsDto extends ABasicAdminDto {
    private Long id;
    private String title;
    private String content;
    private String avatar;
    private String banner;
    private String description;
    private Long categoryId;
    private Integer pinTop;
    private Integer kind;
}
