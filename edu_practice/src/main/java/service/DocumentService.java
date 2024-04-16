package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
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

    @Autowired
    private Tesseract tesseract;

    public Document getDocumentById(long id) {
        return documentRepository.get(id);
    }

    public List<Document> getDocuments() {
        return documentRepository.getAll();
    }

    public void addDocument(MultipartFile raw_document,
                            String document_name,
                            String document_author) {

        LocalDateTime timestamp = LocalDateTime.now();

        // initializing an intermediate file, which is created from raw_file
        File document_file = new File(
                "/home/cutlery/coding/leti/labs_vol.4/edu_practice/src/main/resources/files/"
                + document_name + ".pdf");

        try {
            // physically creating the file
            document_file.createNewFile();
            OutputStream out = new FileOutputStream(document_file);

            // filling up the file
            out.write(raw_document.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text = null;
        if (!(document_file.exists() && !document_file.isDirectory())) {
            return;
        }

        try {
            text = tesseract.doOCR(document_file);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

//        documentRepository.add(document);
    }

    public void deleteDocumentById(long id) {
        documentRepository.delete(id);
    }

}
