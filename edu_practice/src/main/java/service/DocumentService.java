package service;

import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import model.Document;
import repository.DocumentRepository;

@Service
@ComponentScan("repository")
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document getDocumentById(long id) {
        return null;
    }

    public List<Document> getDocuments() {
        return null;
    }

    public void addDocument(MultipartFile raw_document) {
        LocalDateTime timestamp = LocalDateTime.now();

        Document document = new Document(
                123,
                "smth",
                "someone",
                timestamp,
                timestamp,
                "xyu",
                "p 1 zda ");

        documentRepository.add(document);
    }

    public void deleteDocumentById(long id) {

    }

}
