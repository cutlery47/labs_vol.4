package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import service.DocumentService;

import model.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@ComponentScan("service")
// Класс, отвечающий за перенаправление запросов
// на отведенные им функции
public class DocumentController {

    @Autowired
    // Приватный аттрибут
    // Для обеспечения шаблона проектирования Dependency Injection,
    // контроллер в качестве одного из атрибутов хранит низлежащий
    // по иерархии класс DocumentService
    private DocumentService documentService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    // Публичная функция, возвращает ModelAndView
    // Получает GET-запрос -> забирает из базы данных данные о всех файлах -> отрисовывает главное меню
    public ModelAndView get() {
        System.out.println("home");
        List<Document> documents = documentService.getDocuments();
        ModelAndView modelAndView = new ModelAndView("homepage");
        modelAndView.addObject("documents", documents);
        return modelAndView;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    // Публичная функция, возвращаемое значение - String, перенаправляющий на главное меню
    // Получает pdf-файл из POST-запроса -> загружает его в базу данных -> перенаправляет в главное меню
    public String upload(
            @RequestParam MultipartFile raw_document,
            @RequestParam String document_name,
            @RequestParam String document_author) {
        System.out.println("uploading a new user");
        documentService.addDocument(raw_document, document_name, document_author);
        return "redirect:/home/";
    }


    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    // Публичная функция, ничего не возвращает
    // Получает pdf-файл из базы данных -> добавляет этот в файл в тело ответа -> отдает пользователю ответ с файлом
    public void download(@PathVariable long id, HttpServletResponse response)
    throws IOException {
        System.out.println("downloading user with id = " + id);

        File document_file = documentService.downloadDocumentById(id);
        Path file_path = Paths.get(document_file.getAbsolutePath());
        if (Files.exists(file_path)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
            response.addHeader("Content-Disposition", "attachment; filename=\"" + document_file.getName() + "\"");

            try {
                Files.copy(file_path, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    // Публичная функция, возвращаемое значение - String, перенаправляющий на главное меню
    // Получает pdf-файл в базе данных и удаляет его
    public String delete(@PathVariable long id) {
        System.out.println("deleting user with id = " + id);
        documentService.deleteDocumentById(id);
        return "redirect:/home/";
    }
}
