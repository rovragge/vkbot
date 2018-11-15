package dao;


import database.HibernateUtil;
import model.Question;

public class QuestionDao extends BasicDao{
    public Question findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(Question.class, id);
    }
}

