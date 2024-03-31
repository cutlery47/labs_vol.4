package dev.application;

import dev.application.model.Document;

import java.util.Date;

public class Application {
    public static void main(String[] args) {

        Document document = new Document(1, "xyu", "cutlery", new Date());
        System.out.print(document);

    }
}