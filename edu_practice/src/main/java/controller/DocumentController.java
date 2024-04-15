package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import service.DocumentService;

import model.Document;

import java.util.List;

@Controller
@ComponentScan("service")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String getSome() {
        return "home";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void upload() {
        System.out.println("uploading a new user");
        documentService.addDocument(null);
    }

    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public void download(@PathVariable long id) {
        System.out.println("downloading user with id = " + id);
        Document document = documentService.getDocumentById(id);
    }

    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public void load() {
        System.out.println("loading data from the db");
        List<Document> documents = documentService.getDocuments();
        for (Document document : documents) {
            System.out.println(document);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        System.out.println("deleting user with id = " + id);
        documentService.deleteDocumentById(id);
    }
}
