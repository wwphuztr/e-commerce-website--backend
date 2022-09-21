package com.nails.api.storage.repository;

import com.nails.api.storage.model.Customer;
import com.nails.api.storage.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProvinceRepository extends JpaRepository<Province, Long>, JpaSpecificationExecutor<Province>{
}
