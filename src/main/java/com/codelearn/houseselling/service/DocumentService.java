package com.codelearn.houseselling.service;

import com.codelearn.houseselling.dto.DocumentRequest;
import com.codelearn.houseselling.dto.DocumentResponse;
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

    public DocumentResponse createDocument(DocumentRequest request) {

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + request.getHouseId()));

        // Rule 9:
        // A house cannot have two documents with the same document number.
        if (documentRepository.existsByHouseHouseIdAndDocumentNumber(
                request.getHouseId(),
                request.getDocumentNumber())) {

            throw new IllegalArgumentException(
                    "Document number already exists for house: "
                            + request.getHouseId());
        }

        Document document = new Document();

        document.setDocumentName(request.getDocumentName());
        document.setDocumentType(request.getDocumentType());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setIssueDate(request.getIssueDate());
        document.setStatus(request.getStatus());
        document.setHouse(house);

        Document savedDocument = documentRepository.save(document);

        return convertToResponse(savedDocument);
    }

    public List<DocumentResponse> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public DocumentResponse getDocumentById(Long id) {

        Document document =
                documentRepository.findById(id).orElse(null);

        if (document == null) {
            return null;
        }

        return convertToResponse(document);
    }

    public DocumentResponse updateDocument(
            Long id,
            DocumentRequest request) {

        Document existingDocument =
                documentRepository.findById(id).orElse(null);

        if (existingDocument == null) {
            return null;
        }

        House house = houseRepository.findById(request.getHouseId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "House not found with id: " + request.getHouseId()));

        // Rule 9:
        // Prevent another document from using the same number
        // for the same house.
        if (documentRepository.existsByHouseHouseIdAndDocumentNumber(
                request.getHouseId(),
                request.getDocumentNumber())) {

            boolean isSameDocument =
                    existingDocument.getDocumentId().equals(id);

            if (!isSameDocument) {
                throw new IllegalArgumentException(
                        "Document number already exists for house: "
                                + request.getHouseId());
            }
        }

        existingDocument.setDocumentName(request.getDocumentName());
        existingDocument.setDocumentType(request.getDocumentType());
        existingDocument.setDocumentNumber(request.getDocumentNumber());
        existingDocument.setIssueDate(request.getIssueDate());
        existingDocument.setStatus(request.getStatus());
        existingDocument.setHouse(house);

        Document updatedDocument =
                documentRepository.save(existingDocument);

        return convertToResponse(updatedDocument);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    private DocumentResponse convertToResponse(Document document) {

        DocumentResponse response = new DocumentResponse();

        response.setDocumentId(document.getDocumentId());
        response.setDocumentName(document.getDocumentName());
        response.setDocumentType(document.getDocumentType());
        response.setDocumentNumber(document.getDocumentNumber());
        response.setIssueDate(document.getIssueDate());
        response.setStatus(document.getStatus());

        if (document.getHouse() != null) {

            response.setHouseId(
                    document.getHouse().getHouseId()
            );

            response.setHouseTitle(
                    document.getHouse().getTitle()
            );
        }

        return response;
    }
}