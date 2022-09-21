package com.nails.api.storage.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.ListIterator;

@Entity
@Getter
@Setter
@Table(name = TablePrefix.PREFIX_TABLE+"product")
public class Product extends  Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Long price;
    private String image;

    @Column(name = "content", columnDefinition = "TEXT", length=10485760)
    private String description;

    @Column(name = "shortContent", columnDefinition = "TEXT", length=10485760)
    private String shortDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Product parentProduct;

    @OneToMany(mappedBy = "parentProduct", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Product> productList;

    private Boolean hasChild;

    @Column(name = "label_color")
    private String labelColor = "#ffffff00";

    @Column(name = "Quantity_InStock")
    private Integer quantity;

    @Min(0)
    @Max(100)
    private Integer saleoff = 0;
}