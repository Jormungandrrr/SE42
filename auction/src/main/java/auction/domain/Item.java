package auction.domain;

import nl.fontys.util.Money;

import javax.persistence.*;

    @Entity
    @NamedQueries({
            @NamedQuery(name ="Item.count",query="select count(i) from Item as i" ),
            @NamedQuery(name ="Item.findByDescription", query = "select i from Item as i where i.itemDescription = :description"),
            @NamedQuery(name ="Item.findById", query = "select i from Item as i where i.id = :id")
    })
public class Item implements Comparable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User seller;

    @Embedded
    private Category category;

    @Column
    private String itemDescription;

    @OneToOne
    private Bid highest;

    public Item(){
        //Empty constructor for JPA
    }

    public Item(User seller, Category category, String itemDescription) {
        this.seller = seller;
        this.category = category;
        this.itemDescription = itemDescription;
    }

    public Long getId() {
        return id;
    }

    public User getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return itemDescription;
    }

    public Bid getHighestBid() {
        return highest;
    }

    public Bid newBid(User buyer, Money amount) {
        if (highest != null && highest.getAmount().compareTo(amount) >= 0) {
            return null;
        }
        highest = new Bid(buyer, amount);
        return highest;
    }

    public int compareTo(Object arg0) {
        //TODO
        return -1;
    }

    public boolean equals(Object o) {
        //TODO
        return false;
    }

    public int hashCode() {
        //TODO
        return 0;
    }
}
