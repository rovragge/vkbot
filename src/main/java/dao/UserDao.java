package dao;

import database.HibernateUtil;
import model.User;
import org.hibernate.Session;

import java.util.List;

public class UserDao extends BasicDao{

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
        }
    }
}
