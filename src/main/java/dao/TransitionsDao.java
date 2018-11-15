package dao;

import database.HibernateUtil;
import model.Transitions;

public class TransitionsDao extends BasicDao{
    public Transitions findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(Transitions.class, id);
    }
}
