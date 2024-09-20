package org.example.daos;

import org.example.config.HibernateConfig;
import org.example.dtos.CastDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CastDAOTest {

    private static final EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
    private static final CastDAO CAST_DAO = new CastDAO(emfTest);
    private CastDTO actor1; // Første skuespiller som CastDTO
    private CastDTO actor2; // Anden skuespiller som CastDTO

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Actor").executeUpdate(); // Tømmer Actor tabellen
            em.createNativeQuery("ALTER SEQUENCE actor_id_seq RESTART WITH 1").executeUpdate(); // Nulstiller sekvens
            em.getTransaction().commit();

            // Opretter initiale skuespillere
            actor1 = new CastDTO(); // Opretter med standardkonstruktør
            actor1.setName("Robert Downey Jr."); // Sætter navn
            actor1.setCharacter("Iron Man"); // Sætter karakter

            actor2 = new CastDTO(); // Opretter med standardkonstruktør
            actor2.setName("Chris Evans"); // Sætter navn
            actor2.setCharacter("Captain America"); // Sætter karakter

            actor1 = CAST_DAO.createActor(actor1); // Gemmer actor1
            actor2 = CAST_DAO.createActor(actor2); // Gemmer actor2
        }
    }

    @Test
    void createActor() {
        CastDTO actor3 = new CastDTO(); // Opretter med standardkonstruktør
        actor3.setName("Scarlett Johansson"); // Sætter navn
        actor3.setCharacter("Black Widow"); // Sætter karakter

        actor3 = CAST_DAO.createActor(actor3); // Gemmer actor3
        assertNotNull(actor3.getId()); // Sikrer at actor3 har en ID
        assertEquals("Scarlett Johansson", actor3.getName()); // Validerer navn
    }

    @Test
    void getActorById() {
        CastDTO foundActor = CAST_DAO.getActorById(actor1.getId()); // Henter actor1
        assertNotNull(foundActor); // Sikrer at fundet skuespiller ikke er null
        assertEquals(actor1.getName(), foundActor.getName()); // Validerer navn
    }

    @Test
    void updateActor() {
        actor1.setName("Robert Downey Jr. Updated"); // Opdaterer navn
        CastDTO updatedActor = CAST_DAO.updateActor(actor1); // Gemmer ændringer
        assertEquals("Robert Downey Jr. Updated", updatedActor.getName()); // Validerer opdatering
    }

    @Test
    void deleteActor() {
        CAST_DAO.deleteActor(actor2.getId()); // Sletter actor2
        assertNull(CAST_DAO.getActorById(actor2.getId())); // Sikrer at actor2 er slettet
    }

    @Test
    void getAllActors() {
        List<CastDTO> actors = CAST_DAO.getAllActors(); // Henter alle skuespillere
        assertEquals(2, actors.size()); // Validerer antal
    }
}
