package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    boolean existsByHouseHouseIdAndStatus(
            Long houseId,
            String status
    );

    boolean existsByCustomerCustomerId(Long customerId);
}