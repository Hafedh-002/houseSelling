package com.codelearn.houseselling.repository;

import com.codelearn.houseselling.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}