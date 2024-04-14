package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import model.Document;
import repository.DocumentRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    Document getDocumentById(long id) {
        return null;
    }

    List<Document> getDocuments() {
        return null;
    }

    void addDocument(MultipartFile raw_document) {

    }

    void deleteDocumentById(long id) {

    }

}
