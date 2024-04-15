package model;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_author")
    private String documentAuthor;

    @Column(name = "document_uploaded_at")
    private LocalDateTime documentUploadedAt;

    @Column(name = "document_updated_at")
    private LocalDateTime documentUpdatedAt;

    @Column(name = "document_text")
    private String documentText;

    @Column(name = "document_key_words")
    private String documentKeyWords;

    public Document() {}

    public Document(long documentId,
                    String documentName,
                    String documentAuthor,
                    LocalDateTime documentUploadedAt,
                    LocalDateTime documentUpdatedAt,
                    String documentText,
                    String documentKeyWords) {
        super();
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

    public void setDocumentKeyWords(String documentKeyWords) {
        this.documentKeyWords = documentKeyWords;
    }

    public void setDocumentText(String documentText) {
        this.documentText = documentText;
    }

    public void setDocumentUpdatedAt(LocalDateTime documentUpdatedAt) {
        this.documentUpdatedAt = documentUpdatedAt;
    }

    public void setDocumentUploadedAt(LocalDateTime documentUploadedAt) { this.documentUploadedAt = documentUploadedAt; }

    public void setDocumentAuthor(String documentAuthor) {
        this.documentAuthor = documentAuthor;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
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
