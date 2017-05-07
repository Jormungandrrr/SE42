package util;

import bank.domain.Account;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

public abstract class DatabaseCleaner {

    private static final Class<?>[] ENTITY_TYPES = {
        Account.class
    };

    public static void clean(EntityManager entityManager) throws SQLException {
        entityManager.getTransaction().begin();

        for (Class<?> entityType : ENTITY_TYPES) {
            deleteEntities(entityType, entityManager);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @SuppressWarnings("JPQLValidation")
    private static void deleteEntities(Class<?> entityType, EntityManager entityManager) {
        entityManager.createQuery("delete from " + getEntityName(entityType, entityManager)).executeUpdate();
    }

    protected static String getEntityName(Class<?> clazz, EntityManager entityManager) {
        EntityType et = entityManager.getMetamodel().entity(clazz);
        return et.getName();
    }
}