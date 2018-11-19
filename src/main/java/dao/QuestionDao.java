package dao;


import database.HibernateUtil;
import model.Question;
import org.hibernate.Session;

import java.util.List;

public class QuestionDao extends BasicDao{
    public Question findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(Question.class, id);
    }

    public List<Question> findAll(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Question> questions = session.createQuery("select q from Question q").list();
            return questions;
        }
    }
}

