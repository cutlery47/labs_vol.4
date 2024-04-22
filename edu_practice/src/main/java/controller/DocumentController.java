package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import service.DocumentService;

import model.Document;

import java.io.File;
import java.util.List;

@Controller
@ComponentScan("service")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView getSome() {
        System.out.println("home");
        List<Document> documents = documentService.getDocuments();
        ModelAndView modelAndView = new ModelAndView("homepage");
        modelAndView.addObject("documents", documents);
        return modelAndView;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(
            @RequestParam MultipartFile raw_document,
            @RequestParam String document_name,
            @RequestParam String document_author) {
        System.out.println("uploading a new user");
        documentService.addDocument(raw_document, document_name, document_author);
        return "redirect:/home";
    }

    @ResponseBody
    @RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
    public FileSystemResource download(@PathVariable long id) {
        System.out.println("downloading user with id = " + id);
        File document_file = documentService.downloadDocumentById(id);
        return new FileSystemResource(document_file);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable long id) {
        System.out.println("deleting user with id = " + id);
        documentService.deleteDocumentById(id);
        return "redirect:/home";
    }
}
