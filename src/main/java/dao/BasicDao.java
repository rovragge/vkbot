package dao;

import database.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BasicDao {

    public void save(Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(object);
        tx1.commit();
        session.close();
    }

    public void update(Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(object);
        tx1.commit();
        session.close();
    }

    public void delete(Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(object);
        tx1.commit();
        session.close();
    }
}
