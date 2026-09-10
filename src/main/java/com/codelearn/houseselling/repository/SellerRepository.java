package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}