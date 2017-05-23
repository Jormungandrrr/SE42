package auction.dao;

import auction.domain.Item;
import auction.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class ItemDAOJPAImpl implements ItemDAO {

    private final EntityManager em;

    public ItemDAOJPAImpl(EntityManager entityManager) {
        em = entityManager;
    }

    @Override
    public int count() {
        Query q = em.createNamedQuery("Item.count", Item.class);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void create(Item item) {
        em.getTransaction().begin();
        em.persist(item);
        em.getTransaction().commit();
    }

    @Override
    public void edit(Item item) {
        em.getTransaction().begin();
        em.merge(item);
        em.getTransaction().commit();
    }

    @Override
    public Item find(Long id) {
        Query cq = em.createNamedQuery("Item.findById", Item.class);
        cq.setParameter("id", id);
        return (Item) cq.getSingleResult();
    }

    @Override
    public List<Item> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Item.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Item> findByDescription(String description) {
        Query q = em.createNamedQuery("Item.findByDescription", Item.class);
        try {
            return q.setParameter("description", description).getResultList();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void remove(Item item) {
        em.getTransaction().begin();
        em.remove(this.find(item.getId()));
        em.getTransaction().commit();
    }
}
