package com.nails.api.storage.criteria;

import com.nails.api.storage.model.Category;
import com.nails.api.storage.model.Product;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCriteria {
    private Long id;
    private String name;
    private Long categoryId;
    private Integer status;
    private Integer quantity;
    private Long parentId;

    public Specification<Product> getSpecification() {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getParentId() != null) {
                    Join<Product, Product> joinParent = root.join("parentProduct", JoinType.INNER);
                    predicates.add(cb.equal(joinParent.get("id"), getParentId()));
                }
                else {
                    predicates.add(cb.isNull(root.get("parentProduct")));
                }

                if(getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }

                if(!StringUtils.isEmpty(getName())) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }

                if(getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }

                if(getCategoryId() != null) {
                    Join<Product, Category> joinCategory = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"), getCategoryId()));
                }

                if(getQuantity() != null) {
                    predicates.add(cb.equal(root.get("quantity"), getQuantity()));
                }
                criteriaQuery.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
