
        package com.codelearn.houseselling.service;

import com.codelearn.houseselling.entity.Document;
import com.codelearn.houseselling.entity.House;
import com.codelearn.houseselling.repository.DocumentRepository;
import com.codelearn.houseselling.repository.HouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final HouseRepository houseRepository;

    public DocumentService(
            DocumentRepository documentRepository,
            HouseRepository houseRepository) {

        this.documentRepository = documentRepository;
        this.houseRepository = houseRepository;
    }

    public Document createDocument(Document document) {

        if (document.getHouse() == null ||
                document.getHouse().getHouseId() == null) {

            throw new IllegalArgumentException("House is required");
        }

        Long houseId = document.getHouse().getHouseId();

        House house = houseRepository.findById(houseId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + houseId
                        ));

        document.setHouse(house);

        return documentRepository.save(document);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public Document updateDocument(Long id, Document document) {

        Document existingDocument = documentRepository.findById(id)
                .orElse(null);

        if (existingDocument == null) {
            return null;
        }

        if (document.getHouse() == null ||
                document.getHouse().getHouseId() == null) {

            throw new IllegalArgumentException("House is required");
        }

        Long houseId = document.getHouse().getHouseId();

        House house = houseRepository.findById(houseId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + houseId
                        ));

        existingDocument.setDocumentName(document.getDocumentName());
        existingDocument.setDocumentType(document.getDocumentType());
        existingDocument.setDocumentNumber(document.getDocumentNumber());
        existingDocument.setIssueDate(document.getIssueDate());
        existingDocument.setStatus(document.getStatus());
        existingDocument.setHouse(house);

        return documentRepository.save(existingDocument);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}
