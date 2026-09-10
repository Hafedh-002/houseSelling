package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Management;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagementRepository extends JpaRepository<Management, Long> {
}