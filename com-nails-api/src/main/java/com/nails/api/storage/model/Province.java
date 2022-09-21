package com.nails.api.storage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = TablePrefix.PREFIX_TABLE+"province")
public class Province extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Province parentProvince;

    @JsonIgnore
    @OneToMany(mappedBy = "parentProvince", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Province> provinceList;
    private String kind;
}
