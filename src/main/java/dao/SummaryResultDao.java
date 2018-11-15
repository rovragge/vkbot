package dao;


import database.HibernateUtil;
import model.SummaryResult;

public class SummaryResultDao extends BasicDao{
    public SummaryResult findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(SummaryResult.class, id);
    }
}

