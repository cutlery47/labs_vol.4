package dev.application.model;

import java.util.Date;

public final class Document {
    private final int documentId;
    private final String documentName;
    private final String author;
    private final Date createdAt;

    public Document(int documentId, String documentName, String author, Date createdAt) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.author = author;
        this.createdAt = createdAt;
    }


    public int getDocumentId() {
        return this.documentId;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public String getAuthor() {
        return this.author;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public String toString() {
        return "id: " + documentId + "\n" +
                "name: " + documentName + "\n" +
                "author: " + author + "\n" +
                "created at: " + createdAt;
    }
}
