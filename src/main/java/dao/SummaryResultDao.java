package dao;

import database.HibernateUtil;
import model.SummaryResult;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SummaryResultDao {

    public SummaryResult findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(SummaryResult.class, id);
    }

    public void save(SummaryResult user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    public void update(SummaryResult user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(user);
        tx1.commit();
        session.close();
    }

    public void delete(SummaryResult user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }
}
