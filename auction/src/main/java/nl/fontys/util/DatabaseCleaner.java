package nl.fontys.util;

/**
 *
 * @author Marc
 */
import auction.domain.Bid;
import auction.domain.Item;
import auction.domain.User;

import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

public class DatabaseCleaner {

    private static final Class<?>[] ENTITY_TYPES = {
        Item.class,
        User.class,
        Bid.class
    };
    private final EntityManager em;

    public DatabaseCleaner(EntityManager entityManager) {
        em = entityManager;
    }

    public void clean() throws SQLException {
        em.getTransaction().begin();

        for (Class<?> entityType : ENTITY_TYPES) {
            deleteEntities(entityType);
        }
        em.getTransaction().commit();
        em.close();
    }

    private void deleteEntities(Class<?> entityType) {
        em.createQuery("delete from " + getEntityName(entityType)).executeUpdate();
    }

    protected String getEntityName(Class<?> clazz) {
        EntityType et = em.getMetamodel().entity(clazz);
        return et.getName();
    }
}
