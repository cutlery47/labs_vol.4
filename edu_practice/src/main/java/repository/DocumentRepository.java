package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import model.Document;

@Repository
@Transactional
@ComponentScan("config")
// Класс репозитория для работы с базой данных
public class DocumentRepository {

    @Autowired
    // Приватный аттрибут
    // Хранит сессию (подключение) Hibernate ORM
    private SessionFactory sessionFactory;

    // Публичная функция, возвращаемое значение - документ из базы данных
    // Находит и возвращает из базы данных документ с указанным id
    public Document get(long id) {
        Session session = null;
        Transaction transaction = null;
        Document document = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            document = session.get(Document.class, id);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return document;
    }

    // публичная функция, возвращает все документы из базы данных
    public List<Document> getAll() {
        List<Document> documents = null;
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            documents = session.createQuery("from model.Document", Document.class).list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return documents;
    }

    // Публичная функция - ничего не возвращает
    // Добавляет документ в базу данных
    public void add(Document document) {

        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            session.save(document);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // Публичная функция - ничего не возвращает
    // Удаляет документ по указанному id из базы данных
    public void delete(long id) {
        Session session = null;
        Transaction transaction = null;

        Document document = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            document = session.get(Document.class, id);
            session.delete(document);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
