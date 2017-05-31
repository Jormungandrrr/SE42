package auction.domain;

import nl.fontys.util.FontysTime;
import nl.fontys.util.Money;

import javax.persistence.*;

@Entity
public class Bid {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private FontysTime time;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User buyer;

    @ManyToOne(cascade = CascadeType.PERSIST)

    @JoinColumn(nullable = false)
    private Item item;

    private Money amount;

    public Bid(){
        //Empty for JPA
    }

    public Bid(User buyer, Money amount) {
        this.buyer = buyer;
        this.amount = amount;
    }

    public FontysTime getTime() {
        return time;
    }

    public User getBuyer() {
        return buyer;
    }

    public Money getAmount() {
        return amount;
    }

    public Item getItem(){ return item;}
}