package com.nails.api.storage.criteria;

import com.nails.api.storage.model.Account;
import com.nails.api.storage.model.Customer;
import com.nails.api.storage.model.Group;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerCriteria {
    private Long id;
    //private String username;
    private String email;
    private String fullName;
    private String phone;

    public Specification<Customer> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                Join<Customer, Account> joinAccount = root.join("account", JoinType.INNER);
//                if (!StringUtils.isEmpty(getUsername())) {
//                        predicates.add(cb.like(cb.lower(joinAccount.get("username")), "%" + getUsername().toLowerCase() + "%"));
//                }
                if (!StringUtils.isEmpty(getEmail())) {
                    predicates.add(cb.like(cb.lower(joinAccount.get("email")), "%" + getEmail().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getFullName())) {
                    predicates.add(cb.like(cb.lower(joinAccount.get("fullName")), "%" + getFullName().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getPhone())) {
                    predicates.add(cb.like(cb.lower(joinAccount.get("phone")), "%" + getPhone().toLowerCase() + "%"));
                }
                query.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
