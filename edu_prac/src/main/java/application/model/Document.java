package application.model;

import java.io.ByteArrayInputStream;
import java.util.Date;

public record Document(int documentId, String documentName, String author, Date createdAt, Date updatedAt,
                       ByteArrayInputStream binaryDocumentData, String documentData) {

    @Override
    public String toString() {
        return "id: " + documentId + "\n" +
                "name: " + documentName + "\n" +
                "author: " + author + "\n" +
                "created at: " + createdAt + "\n" +
                "updated at: " + updatedAt + "\n";
    }
}
