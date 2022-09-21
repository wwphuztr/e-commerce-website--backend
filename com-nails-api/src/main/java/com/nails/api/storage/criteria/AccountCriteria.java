package com.nails.api.storage.criteria;

import com.nails.api.storage.model.Account;
import com.nails.api.storage.model.Group;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class AccountCriteria  {
    private Long id;
    private Integer kind;
    private String username;
    private String email;
    private String fullname;
    private Long groupId;
    private String phone;

    public Specification<Account> getSpecification() {
        return new Specification<Account>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getKind() != null){
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(getGroupId() != null){
                    Join<Account, Group> joinGroup = root.join("group", JoinType.INNER);
                    predicates.add(cb.equal(joinGroup.get("id"), getGroupId()));
                }
                if(!StringUtils.isEmpty(getUsername())){
                    predicates.add(cb.like(cb.lower(root.get("username")), "%"+getUsername().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getEmail())){
                    predicates.add(cb.like(cb.lower(root.get("email")), "%"+getEmail().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getFullname())){
                    predicates.add(cb.like(cb.lower(root.get("fullname")), "%"+getFullname().toLowerCase()+"%"));
                }

                if(!StringUtils.isEmpty(getPhone())){
                    predicates.add(cb.like(cb.lower(root.get("phone")), "%"+getPhone().toLowerCase()+"%"));
                }
                query.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
