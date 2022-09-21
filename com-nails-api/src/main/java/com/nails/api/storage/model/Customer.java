package com.nails.api.storage.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"customer")
public class Customer extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id")
    @MapsId
    private Account account;


    @Column(name = "`address`")
    private String address;

    private Date birthday;
    private Integer sex;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    private Date loyaltyDate;
    private Boolean isLoyalty = false;
    private Integer loyaltyLevel;
    private Integer saleOff = 0;
}
