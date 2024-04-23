package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "document_name")
    private String documentName = "unknown";

    @Column(name = "document_author")
    private String documentAuthor = "unknown";

    @Column(name = "document_uploaded_at")
    private String documentUploadedAt;

    @Column(name = "document_updated_at")
    private String documentUpdatedAt;

    @Column(name = "document_text", columnDefinition = "TEXT")
    private String documentText;

    @Column(name = "document_binary", length=65536)
    private byte[] documentBinary;

    @Column(name = "document_key_words")
    private String documentKeyWords;

    public Document() {}

    public Document(long documentId,
                    String documentName,
                    String documentAuthor,
                    String documentUploadedAt,
                    String documentUpdatedAt,
                    String documentText,
                    byte[] documentBinary,
                    String documentKeyWords) {
        super();
        this.documentId = documentId;
        if (!Objects.equals(documentName, "")) {
            this.documentName = documentName;
        }
        if (!Objects.equals(documentAuthor, "")) {
            this.documentAuthor = documentAuthor;
        }
        this.documentUploadedAt = documentUploadedAt;
        this.documentUpdatedAt = documentUpdatedAt;
        this.documentText = documentText;
        this.documentBinary = documentBinary;
        this.documentKeyWords = documentKeyWords;
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
