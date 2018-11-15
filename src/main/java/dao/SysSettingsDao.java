package dao;

import database.HibernateUtil;
import model.SysSettings;

public class SysSettingsDao extends BasicDao{
    public SysSettings findById(Long id) {
        return HibernateUtil.getSessionFactory().openSession().get(SysSettings.class, id);
    }

}



