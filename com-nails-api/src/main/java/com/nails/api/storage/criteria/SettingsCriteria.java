/*
 * File Created: Monday, 1st February 2021 7:18:33 pm Author: Bui Si Quan
 * (18110041@student.hcmute.edu.vn) -----
 */
package com.nails.api.storage.criteria;

import com.nails.api.storage.model.Settings;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
public class SettingsCriteria {

    private Long id;
    private String name;
    private String key;
    private String value;
    private String group;

    public Specification<Settings> getSpecification() {
        return new Specification<Settings>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Settings> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getName() != null) {
                    predicates.add(cb.equal(root.get("name"), getName()));
                }
                if (getKey() != null) {
                    predicates.add(cb.equal(root.get("key"), getKey()));
                }
                if (getValue() != null) {
                    predicates.add(cb.equal(root.get("value"), getValue()));
                }
                if (getGroup() != null) {
                    predicates.add(cb.equal(root.get("group"), getGroup()));
                }
                query.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    public Specification<Settings> listByName() {
        return new Specification<Settings>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Settings> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("name"), getName());
            }
        };
    }
}
