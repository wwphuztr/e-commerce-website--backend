package com.nails.api.storage.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"news")
public class News extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;


    private String content;
    private String avatar;
    private String banner;

    @Column(name = "long_content", columnDefinition = "TEXT", length=10485760)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    private Integer pinTop = 0; // 0 unpin, 1 pin
    private Integer kind; // 0 internal, ctv
}
