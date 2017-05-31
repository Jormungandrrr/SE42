package auction.domain;

import java.util.Iterator;
import java.util.Set;
import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "User.count", query = "select count(u) from User as u")
    ,
        @NamedQuery(name = "User.findByEmail", query = "select u from User as u where u.email = :email")
})
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<Item> offeredItems;

    public User(String email) {
        this.email = email;

    }
    public User() {

    }

    public Iterator getOfferedItems(){
        return offeredItems.iterator();
    }

    public void addOfferedItems(Item item){
        offeredItems.add(item);
    }

    public int numberOfOfferedItems(){
        return offeredItems.size();
    }

    public String getEmail() {
        return email;
    }
}

