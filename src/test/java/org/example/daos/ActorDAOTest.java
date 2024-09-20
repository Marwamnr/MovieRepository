package org.example.daos;

import org.example.config.HibernateConfig;
import org.example.dtos.ActorDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActorDAOTest {

    private static final EntityManagerFactory emfTest = HibernateConfig.getEntityManagerFactoryForTest();
    private static final ActorDAO actorDAO = new ActorDAO(emfTest);
    private ActorDTO actor1;
    private ActorDTO actor2;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emfTest.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Actor").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE actor_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();

            // Creating initial actors
            actor1 = new ActorDTO(null, "Robert Downey Jr.", null);
            actor2 = new ActorDTO(null, "Chris Evans", null);

            actor1 = actorDAO.createActor(actor1);
            actor2 = actorDAO.createActor(actor2);
        }
    }

    @Test
    void createActor() {
        ActorDTO actor3 = new ActorDTO(null, "Scarlett Johansson", null);
        actor3 = actorDAO.createActor(actor3);
        assertNotNull(actor3.getId());
        assertEquals("Scarlett Johansson", actor3.getName());
    }

    @Test
    void getActorById() {
        ActorDTO foundActor = actorDAO.getActorById(actor1.getId());
        assertNotNull(foundActor);
        assertEquals(actor1.getName(), foundActor.getName());
    }

    @Test
    void updateActor() {
        actor1.setName("Robert Downey Jr. Updated");
        ActorDTO updatedActor = actorDAO.updateActor(actor1);
        assertEquals("Robert Downey Jr. Updated", updatedActor.getName());
    }

    @Test
    void deleteActor() {
        actorDAO.deleteActor(actor2.getId());
        assertNull(actorDAO.getActorById(actor2.getId()));
    }

    @Test
    void getAllActors() {
        List<ActorDTO> actors = actorDAO.getAllActors();
        assertEquals(2, actors.size());
    }
}
