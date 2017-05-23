package auction.service;

import auction.dao.ItemDAOJPAImpl;
import auction.domain.Category;
import auction.domain.Item;
import auction.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class SellerMgr {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auction");
    private ItemDAOJPAImpl itemDAO;

    public SellerMgr() {
        EntityManager em = emf.createEntityManager();
        itemDAO = new ItemDAOJPAImpl(em);
    }

    /**
     * @param seller
     * @param cat
     * @param description
     * @return het item aangeboden door seller, behorende tot de categorie cat
     *         en met de beschrijving description
     */
    public Item offerItem(User seller, Category cat, String description) {
        Item item = new Item(seller,cat,description);
        itemDAO.create(item);
        return item;
    }
    
     /**
     * @param item
     * @return true als er nog niet geboden is op het item. Het item word verwijderd.
     *         false als er al geboden was op het item.
     */
    public boolean revokeItem(Item item) {
        Item inDB = itemDAO.find(item.getId());
        if(inDB.getHighestBid()== null)
        {
            itemDAO.remove(inDB);
            return true;
        }
        return false;
    }
}
