package database;

import model.DialogState;
import model.MessageVK;
import model.Transitions;
import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Transitions.class);
                configuration.addAnnotatedClass(DialogState.class);
                configuration.addAnnotatedClass(MessageVK.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}