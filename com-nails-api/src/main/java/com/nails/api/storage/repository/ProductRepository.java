package com.nails.api.storage.repository;

import com.nails.api.storage.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    public Integer countProductByParentProductId(Long parentId);

    @Query(value = "SELECT p " +
            "FROM Product p " +
            "order by purchase_count desc")
    public List<Product> topsales(Pageable pageable);

    public List<Product> findByOrderByPurchaseCountDesc(Pageable pageable);
}
