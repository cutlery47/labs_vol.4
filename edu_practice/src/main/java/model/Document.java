package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Basic;
import javax.persistence.GenerationType;

import java.time.LocalDateTime;

@Entity
public class Document {

    @Id
    @Basic
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long documentId;

    @Basic
    private String documentName;

    @Basic
    private String documentAuthor;

    @Basic
    private LocalDateTime documentUploadedAt;

    @Basic
    private LocalDateTime documentUpdatedAt;

    @Basic
    private String documentText;

    @Basic
    private String documentKeyWords;

    public Document() {}

    public Document(long documentId,
                    String documentName,
                    String documentAuthor,
                    LocalDateTime documentUploadedAt,
                    LocalDateTime documentUpdatedAt,
                    String documentText,
                    String documentKeyWords) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentAuthor = documentAuthor;
        this.documentUploadedAt = documentUploadedAt;
        this.documentUpdatedAt = documentUpdatedAt;
        this.documentText = documentText;
        this.documentKeyWords = documentKeyWords;
    }

    public long getDocumentId() {
        return documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public String getDocumentAuthor() {
        return documentAuthor;
    }

    public LocalDateTime getDocumentUpdatedAt() {
        return documentUpdatedAt;
    }

    public LocalDateTime getDocumentUploadedAt() {
        return documentUploadedAt;
    }

    public String getDocumentText() {
        return documentText;
    }

    public String getDocumentKeyWords() {
        return documentKeyWords;
    }

    public String toString() {
        return ("id: " + documentId +
                ", name: " + documentName +
                ", author: " + documentAuthor +
                ", text: " + documentText +
                ", updated at: " + documentUpdatedAt +
                ", uploaded at: " + documentUploadedAt +
                ", key words: " + documentKeyWords);
    }

}
