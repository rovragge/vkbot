package dao;

import database.HibernateUtil;
import model.Transitions;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransitionsDao {
    public Transitions findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(Transitions.class, id);
    }

    public void save(Transitions transitions) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(transitions);
        tx1.commit();
        session.close();
    }

    public void update(Transitions transitions) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(transitions);
        tx1.commit();
        session.close();
    }

    public void delete(Transitions transitions) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(transitions);
        tx1.commit();
        session.close();
    }
}
