package dao;


import database.HibernateUtil;
import model.MessageVK;
import model.User;
import org.hibernate.Session;

import java.util.List;

public class MessageVKDao extends BasicDao{
    public MessageVK findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(MessageVK.class, id);
    }
    public MessageVK findLastByUserAndState(User user, Long stateId){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List messages = session.createQuery(
                    "select msg from MessageVK msg where msg.user = :user and msg.state.id = :state order by msg.id desc")
                    .setParameter("user", user)
                    .setParameter("state", stateId)
                    .setMaxResults(1)
                    .list();
            if (messages.isEmpty()) return null;
            return (MessageVK) messages.get(0);
        }
    }
}

