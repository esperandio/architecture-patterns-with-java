package app.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    public Session create() {
        var configuration = new Configuration();

        configuration.configure("hibernate.cfg.xml");

        // Create Session Factory
        SessionFactory sessionFactory = configuration.buildSessionFactory();
 
        // Initialize Session Object
        return sessionFactory.openSession();
    }
}
