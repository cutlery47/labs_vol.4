package repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.hibernate.Transaction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import model.Document;

import javax.transaction.Transactional;

//import javax.persistence.Table;

@Repository
@Transactional
@ComponentScan("config")
//@Table(name = "documents")
public class DocumentRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public Document get(long id) {
        return null;
    }

    public List<Document> getAll() {
        return null;
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

    }
}
