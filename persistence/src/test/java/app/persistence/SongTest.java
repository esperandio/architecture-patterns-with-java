package app.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SongTest {
    private Session session;

    public SongTest() {
        var configuration = new Configuration();

        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Song.class);

        // Create Session Factory
        SessionFactory sessionFactory = configuration.buildSessionFactory();
 
        // Initialize Session Object
        this.session = sessionFactory.openSession();
    }

    @BeforeEach
    public void initEach() {
        this.session.doWork(connection -> {
            connection.prepareStatement("TRUNCATE TABLE song").executeUpdate();
        });
    }

    @Test
    void testPersistNewSong() {
        Song song = new Song();
 
        song.setId(1);
        song.setSongName("Broken Angel");
        song.setArtist("Akon");
 
        this.session.beginTransaction();
        this.session.persist(song); 
        this.session.getTransaction().commit();

        assertTrue(true);
    }
}
