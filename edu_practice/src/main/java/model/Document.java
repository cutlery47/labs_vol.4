package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.sql.Time;
import java.util.Objects;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "documents")
// Класс, являющийся представлением таблицы Документов в базе данных
// Используется в Hibetnate ORM для работы с данными
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    // id документа
    private Long documentId;

    @Column(name = "document_name")
    // имя документа
    private String documentName = "unknown";

    @Column(name = "document_author")
    // автор документа
    private String documentAuthor = "unknown";

    @Column(name = "document_uploaded_at")
    // время загрузки документа
    private Timestamp documentUploadedAt;

    @Column(name = "document_updated_at")
    // время обновления документа
    private Timestamp documentUpdatedAt;

    @Column(name = "document_text", columnDefinition = "TEXT")
    // текст документа
    private String documentText;

    @Column(name = "document_binary", length=65536)
    // бинарное представление документа
    private byte[] documentBinary;

    @Column(name = "document_key_words")
    // ключевые слова в документе
    private String documentKeyWords;

    public Document() {}

    // конструктор класса
    public Document(long documentId,
                    String documentName,
                    String documentAuthor,
                    Timestamp documentUploadedAt,
                    Timestamp documentUpdatedAt,
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

    // функция преобразования объекта класса в строку
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
