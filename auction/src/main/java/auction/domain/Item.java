package auction.domain;

import nl.fontys.util.Money;

import javax.persistence.*;
import java.util.List;

@Entity
    @NamedQueries({
            @NamedQuery(name ="Item.count",query="select count(i) from Item as i" ),
            @NamedQuery(name ="Item.findByDescription", query = "select i from Item as i where i.itemDescription = :description"),
            @NamedQuery(name ="Item.findById", query = "select i from Item as i where i.id = :id")

    })
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS)
public class Item implements Comparable {

    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User seller;

    @Embedded
    private Category category;

    @Column
    private String itemDescription;

    @OneToOne
    private Bid highest;

    @OneToMany(mappedBy="item", cascade = CascadeType.PERSIST)
    private List<Bid> bids;

    public Item(){
        //Empty constructor for JPA
    }

    public Item(User seller, Category category, String itemDescription) {
        this.seller = seller;
        this.category = category;
        this.itemDescription = itemDescription;
        seller.addOfferedItems(this);
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

    public List<Bid> getBids() {
        return this.bids;
    }

    public Bid newBid(User buyer, Money amount) {
        if (highest != null && highest.getAmount().compareTo(amount) >= 0) {
            return null;
        }
        highest = new Bid(buyer, amount);
        this.bids.add(highest);
        return highest;
    }

    public int compareTo(Object arg0) {
        //TODO
        return -1;
    }

    public boolean equals(Object o) {
        if(this.getClass() != o.getClass()){
            return false;
        }
        Item itemToCompare = (Item) o;

        if(this.id != itemToCompare.id){
            return false;
        }

        if(this.getSeller() != itemToCompare.getSeller()){
            return false;
        }

        if(this.getDescription() != itemToCompare.getDescription()){
            return false;
        }

        return true;
    }

    public int hashCode() {
        int i = 2;
        if(getDescription()!= null){
            i += 5 * getDescription().hashCode();
        }
        if(getCategory() != null){
            i+=4 * getCategory().hashCode();
        }
        return i;
    }
}
