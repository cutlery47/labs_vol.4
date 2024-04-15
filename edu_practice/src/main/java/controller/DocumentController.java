package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import service.DocumentService;

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
    public String upload() {
        //null for the sake of test
        documentService.addDocument(null);
        System.out.println("123123123");
        return "home";
    }


}
