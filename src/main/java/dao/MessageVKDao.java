package dao;


import database.HibernateUtil;
import model.MessageVK;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MessageVKDao {

    public MessageVK findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(MessageVK.class, id);
    }

    public void save(MessageVK message) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(message);
        tx1.commit();
        session.close();
    }

    public void update(MessageVK message) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(message);
        tx1.commit();
        session.close();
    }

    public void delete(MessageVK message) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(message);
        tx1.commit();
        session.close();
    }
}

