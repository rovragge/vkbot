package dao;


import database.HibernateUtil;
import model.MessageVK;

public class MessageVKDao extends BasicDao{
    public MessageVK findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(MessageVK.class, id);
    }
}

