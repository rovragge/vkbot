package dao;

import database.HibernateUtil;
import model.DialogState;

public class DialogStateDao extends BasicDao{
    public DialogState findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(DialogState.class, id);
    }
}



