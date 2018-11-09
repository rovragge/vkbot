package dao;

import database.HibernateSessionFactoryUtil;
import model.Transitions;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TransitionsDao {
    public Transitions findById(Long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Transitions.class, id);
    }

    public void save(Transitions Transitions) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(Transitions);
        tx1.commit();
        session.close();
    }

    public void update(Transitions Transitions) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(Transitions);
        tx1.commit();
        session.close();
    }

    public void delete(Transitions Transitions) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(Transitions);
        tx1.commit();
        session.close();
    }
}
