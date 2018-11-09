package dao;

import database.HibernateSessionFactoryUtil;
import model.DialogState;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DialogStateDao {
    public DialogState findById(Long id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(DialogState.class, id);
    }

    public void save(DialogState dialogState) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(dialogState);
        tx1.commit();
        session.close();
    }

    public void update(DialogState dialogState) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(dialogState);
        tx1.commit();
        session.close();
    }

    public void delete(DialogState dialogState) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(dialogState);
        tx1.commit();
        session.close();
    }
}


