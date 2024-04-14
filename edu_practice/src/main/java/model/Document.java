package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Basic;

import java.sql.Timestamp;

@Entity
public class Document {

    @Id
    @Basic
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long documentId;

    @Basic
    private String documentName;

    @Basic
    private String documentAuthor;

    @Basic
    private Timestamp documentUploadedAt;

    @Basic
    private Timestamp documentUpdatedAt;

    @Basic
    private String documentText;

    public Document() {}

    public Document(long documentId,
                    String documentName,
                    String documentAuthor,
                    Timestamp documentUploadedAt,
                    Timestamp documentUpdatedAt,
                    String documentText) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.documentAuthor = documentAuthor;
        this.documentUploadedAt = documentUploadedAt;
        this.documentUpdatedAt = documentUpdatedAt;
        this.documentText = documentText;
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

    public String getDocumentText() {
        return documentText;
    }

    public Timestamp getDocumentUpdatedAt() {
        return documentUpdatedAt;
    }

    public Timestamp getDocumentUploadedAt() {
        return documentUploadedAt;
    }


}
