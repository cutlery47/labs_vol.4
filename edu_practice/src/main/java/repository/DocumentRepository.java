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
public class DocumentRepository {

    @Autowired
    private SessionFactory sessionFactory;

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

    public void add(Document document) {
        System.out.println(document);

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
