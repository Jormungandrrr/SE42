package auction.dao;

import auction.domain.User;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDAOJPAImp implements UserDAO {

    private final EntityManager em;

    public UserDAOJPAImp(EntityManager entityManager) {
        em = entityManager;
    }

    @Override
    public int count() {
        Query q = em.createNamedQuery("User.count",User.class);
        return ((Long) q.getSingleResult()).intValue();

    }

    @Override
    public void create(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public void edit(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }


    @Override
    public List<User> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(User.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public User findByEmail(String email) {
        Query q = em.createNamedQuery("User.findByEmail",User.class);
        q.setParameter("email",email);
        try {
            return (User) q.getSingleResult();
        }
        catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public void remove(User user) {
        em.getTransaction().begin();
        em.remove(em.merge(user));
        em.getTransaction().commit();
    }
}
