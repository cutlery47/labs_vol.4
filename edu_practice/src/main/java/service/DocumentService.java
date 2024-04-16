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
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import model.Document;
import repository.DocumentRepository;

import static org.bytedeco.leptonica.global.lept.pixDestroy;
import static org.bytedeco.leptonica.global.lept.pixRead;

//class Person {
//
//    int age;
//    int name;
//    int sex;
//
//    return Tesseract;
//
//}

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

//        File document_file = new File("/home/cutlery/coding/leti/labs_vol.4/edu_practice/src/main/java/img.png");

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

//        BytePointer outText;
//
//        TessBaseAPI api = new TessBaseAPI();
//
//        if (api.Init(null, "eng") != 0) {
//            System.out.println("Cound init tesseract");
//        }
//
//        PIX image = pixRead("/home/cutlery/coding/leti/labs_vol.4/edu_practice/src/main/java/img.png");
//        api.SetImage(image);
//
//        outText = api.GetUTF8Text();
//        System.out.println(outText.getString());
//
//        api.End();
//        outText.deallocate();
//        pixDestroy(image);


    }

    public void deleteDocumentById(long id) {
        documentRepository.delete(id);
    }

}
