package dao;

import database.HibernateUtil;
import model.Question;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class QuestionDao {

    public Question findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(Question.class, id);
    }

    public void save(Question user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    public void update(Question user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(user);
        tx1.commit();
        session.close();
    }

    public void delete(Question user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }
}
