package com.nails.api.storage.criteria;

import com.nails.api.storage.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrdersCriteria {

    private Long id;
    private Integer state;
    private String code;
    private Date from;
    private Date to;
    private String customerPhone;
    private Long customerId;

    public Specification<Orders> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(!StringUtils.isEmpty(getCustomerPhone())) {
                    Join<Orders, Customer> joinCustomer = root.join("customer", JoinType.INNER);
                    Join<Join<Orders, Customer>, Account> joinCustomerAccount = joinCustomer.join("account", JoinType.INNER);
                    predicates.add(cb.like(cb.lower(joinCustomerAccount.get("phone")), "%" + getCustomerPhone().toLowerCase() + "%"));
                }

                if(!StringUtils.isEmpty(getCustomerId())) {
                    Join<Orders, Customer> joinCustomer = root.join("customer", JoinType.INNER);
                    predicates.add(cb.equal(joinCustomer.get("id"), getCustomerId()));
                }

                if(!StringUtils.isEmpty(getCode())) {
                    predicates.add(cb.like(cb.lower(root.get("code")), "%" + getCode().toLowerCase() + "%"));
                }

                if(getState() != null) {
                    predicates.add(cb.equal(root.get("state"), getState()));
                }

                if(getFrom() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), getFrom()));
                }

                if(getTo() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), getTo()));
                }

                criteriaQuery.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
