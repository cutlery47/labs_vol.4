package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
// Класс сервиса, ответственный за бизнес-логику
public class DocumentService {

    @Autowired
    // Приватный аттрибут
    // Для обеспечения шаблона проектирования Dependency Injection,
    // контроллер в качестве одного из атрибутов хранит низлежащий
    // по иерархии класс DocumentRepository
    private DocumentRepository documentRepository;

    @Autowired
    // Приватный аттрибут
    // Хранит объект класса Tesseract, используемый для обработки pdf-файлов
    private Tesseract tesseract;

    // Публичная функция - возвращает документ из базы данных
    // Вызывает соответствующую функцию репозитория
    public Document getDocumentById(long id) {

        return documentRepository.get(id);
    }

    // Публичная функция - возвращает все документы из базы данных
    // Вызывает соответствующую функцию репозитория
    public List<Document> getDocuments() {

        return documentRepository.getAll();
    }

    // Публичная функция - ничего не возвращает
    // Преобразует MultipartFile в File -> прогоняет распознование Tesseract-ом -> создает сущность Document ->
    // передает Document в репозиторий
    public void addDocument(MultipartFile raw_document,
                            String document_name,
                            String document_author) {

        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formated_timestamp = timestamp.format(formatter);

        // initializing an intermediate file, which is created from raw_file
        File document_file = new File(
                "C:\\Programming\\leti\\edu_practice\\src\\main\\resources\\files\\"
                + document_name + ".pdf");

        String recognized_text = "";
        byte[] document_binary = null;
        try {
            // physically creating the file and filling it up
            document_binary = raw_document.getBytes();
            document_file.createNewFile();
            OutputStream out = Files.newOutputStream(document_file.toPath());
            out.write(document_binary);

            // tesseract file recognition
            recognized_text = tesseract.doOCR(document_file);

            document_file.delete();
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
                most_common.append(entry.getKey()).append(" - (").append(entry.getValue().toString()).append("); \n");
            }
            i += 1;
        }

        Document document = new Document(
                0,
                document_name,
                document_author,
                formated_timestamp,
                formated_timestamp,
                recognized_text,
                document_binary,
                most_common.toString());

        documentRepository.add(document);
    }
    // Публичная функция - ничего не возвращает
    // Вызывает соответствующую функцию репозитория по удалению файла
    public void deleteDocumentById(long id) {
        documentRepository.delete(id);
    }

    // Публичная функция - возвращает воссозданный pdf-файл из базы данных
    // Создает заглушку -> записывает в заглушку бинарные данные -> передает заглушку пользователю
    public File downloadDocumentById(long id) {
        Document document = documentRepository.get(id);
        File document_file = new File("C:\\Programming\\leti\\edu_practice\\src\\main\\resources\\files\\"
                + document.getDocumentName() + ".pdf");

        try {
            document_file.createNewFile();
            OutputStream out = Files.newOutputStream(document_file.toPath());
            out.write(document.getDocumentBinary());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document_file;

    }

    // Публичная функция - возвращает ХэшМап, ключи которого - слова в тексте, значения - количество повторений в тексте
    // Сортирует неотсортированный хэшмап по значениям
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
