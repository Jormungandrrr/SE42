package auction.domain;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = "User.count", query = "select count(u) from User as u")
    ,
        @NamedQuery(name = "User.findByEmail", query = "select u from User as u where u.email = :email")
})
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    public User(String email) {
        this.email = email;

    }

    public User() {

    }

    public String getEmail() {
        return email;
    }
}
