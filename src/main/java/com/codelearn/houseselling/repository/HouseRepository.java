package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {
}