package auction.service;

import auction.dao.ItemDAOJPAImpl;
import auction.domain.*;

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
        seller.addOfferedItems(item);
        return item;
    }
    public Item offerFurniture(User seller, Category cat, String description, String material) {
        Furniture item = new Furniture(material,seller,cat,description);
        itemDAO.create(item);
        seller.addOfferedItems(item);
        return item;
    }

    public Item offerPainting(User seller, Category cat, String description, String title, String painter) {
        Painting item = new Painting(title,painter,seller,cat,description);
        itemDAO.create(item);
        seller.addOfferedItems(item);
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
