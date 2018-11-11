package dao;

import database.HibernateUtil;
import model.SysSettings;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SysSettingsDao {
    public SysSettings findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(SysSettings.class, id);
    }

    public void save(SysSettings sysSettings) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(sysSettings);
        tx1.commit();
        session.close();
    }

    public void update(SysSettings sysSettings) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(sysSettings);
        tx1.commit();
        session.close();
    }

    public void delete(SysSettings sysSettings) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(sysSettings);
        tx1.commit();
        session.close();
    }
}



