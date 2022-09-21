package com.nails.api.storage.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"orders")
public class Orders extends Auditable<String>  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Integer saleOff = 0;
    private Double totalMoney;
    private Integer vat; // VAT

    private Integer state; // 0 created, 1. accepted(da thanh toan), 2 Shipping, 3 done, 4 cancel

    @Column(name = "prev_state")
    private Integer prevState;

    private String document; //image path Type DOCUMENT -> uy nhiem chi chuyen khoan
    private String address; // Dia chi giao hang
    private String receiverName; // Ten nguoi nhan
    private String receiverPhone; // Sdt nguoi nhan

    private String code;

    private Integer paymentMethod;
}
