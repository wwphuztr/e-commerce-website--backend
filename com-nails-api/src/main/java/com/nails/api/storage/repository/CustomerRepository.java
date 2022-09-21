package com.nails.api.storage.repository;

import com.nails.api.constant.NailsConstant;
import com.nails.api.storage.model.Account;
import com.nails.api.storage.model.Customer;
import com.nails.api.storage.model.TablePrefix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    public Customer findByAccountPhone(String phone);

    @Query(value = "SELECT c" +
            " FROM Customer c" +
            " WHERE (c.account.phone = :phoneOrEmail" +
            " OR c.account.email = :phoneOrEmail)")
    public Customer findBy(@Param("phoneOrEmail") String phoneOrEmail);

    @Query(value = "SELECT COALESCE(count(*), 0)" +
            " FROM Customer c" +
            " WHERE c.account.phone = :phone")
    public Long countBy(@Param("phone") String phone);
}
