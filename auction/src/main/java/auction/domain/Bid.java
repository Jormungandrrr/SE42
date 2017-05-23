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

    @OneToOne
    private User buyer;


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
}


