package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

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
                "C:\\Programming\\leti\\edu_practice\\src\\main\\resources\\files\\"
                + document_name + ".pdf");

        String recognized_text = "";
        try {
            // physically creating the file and filling it up
            document_file.createNewFile();
            OutputStream out = Files.newOutputStream(document_file.toPath());
            out.write(raw_document.getBytes());

            // tesseract file recognition
            recognized_text = tesseract.doOCR(document_file);
        } catch (TesseractException | IOException e) {
            e.printStackTrace();
            return;
        }

        HashMap<String, Integer> words_freq = new HashMap<>();
        String[] words = recognized_text.split("[\\p{Punct}\\s]+");
        for (String word : words) {
            if (word.length() >= 5) {
                words_freq.put(word, words_freq.getOrDefault(word, 0) + 1);
            }
        }

        words_freq = sortByValue(words_freq);

        int i = 0;
        StringBuilder most_common = new StringBuilder();
        for (Map.Entry<String, Integer> entry : words_freq.entrySet()) {
            if (i > words_freq.size() - 6) {
                most_common.append(entry.getKey()).append("; ");
            }
            i += 1;
        }

        Document document = new Document(
                0,
                document_name,
                document_author,
                timestamp,
                timestamp,
                recognized_text,
                most_common.toString());

        documentRepository.add(document);
    }

    public void deleteDocumentById(long id) {
        documentRepository.delete(id);
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
