package database;

import dao.DialogStateDao;
import dao.TransitionsDao;
import dao.UserDao;

public class DaoFactory {

    private static UserDao userDao = null;
    private static TransitionsDao transitionsDao = null;
    private static DialogStateDao dialogStateDao = null;
    private static DaoFactory instance = null;

    public static synchronized DaoFactory getInstance(){
        if (instance == null){
            instance = new DaoFactory();
        }
        return instance;
    }

    public UserDao getUserDao(){
        if (userDao == null){
            userDao = new UserDao();
        }
        return userDao;
    }

    public TransitionsDao getTransitionsDao(){
        if (transitionsDao == null){
            transitionsDao = new TransitionsDao();
        }
        return transitionsDao;
    }

    public DialogStateDao getDialogStateDao(){
        if (dialogStateDao == null){
            dialogStateDao = new DialogStateDao();
        }
        return dialogStateDao;
    }
}
