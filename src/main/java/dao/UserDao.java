package dao;

import database.HibernateUtil;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDao {

    public User findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(User.class, id);
    }

    public User findByVkID(Integer vkID){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List users = session.createQuery("select u from User u where u.vkID = :vkId")
                    .setParameter("vkId", vkID)
                    .list();
            if (users.isEmpty()) return null;
            return (User) users.get(0);
        } }

    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(user);
        tx1.commit();
        session.close();
    }

    public void update(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.merge(user);
        tx1.commit();
        session.close();
    }

    public void delete(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(user);
        tx1.commit();
        session.close();
    }
}
