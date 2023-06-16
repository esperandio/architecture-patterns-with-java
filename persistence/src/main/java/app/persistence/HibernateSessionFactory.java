package app.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    public Session create() {
        var configuration = new Configuration();

        configuration.configure("hibernate.cfg.xml");

        configuration.setProperty("hibernate.connection.url", System.getenv("DATABASE_URL"));
        configuration.setProperty("hibernate.connection.username", System.getenv("DATABASE_USERNAME"));
        configuration.setProperty("hibernate.connection.password", System.getenv("DATABASE_PASSWORD"));

        // Create Session Factory
        SessionFactory sessionFactory = configuration.buildSessionFactory();
 
        // Initialize Session Object
        return sessionFactory.openSession();
    }
}
