package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @Getter
    @Column(name = "document_name")
    private String documentName = "unknown file";

    @Getter
    @Column(name = "document_author")
    private String documentAuthor = "unknown author";

    @Getter
    @Column(name = "document_uploaded_at")
    private LocalDateTime documentUploadedAt;

    @Getter
    @Column(name = "document_updated_at")
    private LocalDateTime documentUpdatedAt;

    @Getter
    @Column(name = "document_text", columnDefinition = "TEXT")
    private String documentText;

    @Getter
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
        if (!Objects.equals(documentName, "")) {
            this.documentName = documentName;
        }
        if (!Objects.equals(documentAuthor, "")) {
            this.documentAuthor = documentAuthor;
        }
        this.documentUploadedAt = documentUploadedAt;
        this.documentUpdatedAt = documentUpdatedAt;
        this.documentText = documentText;
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
