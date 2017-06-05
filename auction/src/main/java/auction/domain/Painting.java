package auction.domain;

import javax.persistence.Entity;

@Entity
public class Painting extends Item{

    private String title;
    private String painter;

    public Painting(){
        //Empty constructor for JPA
    }

    public Painting(String title, String painter, User seller, Category category, String description) {
        super(seller, category, description);
        this.painter = painter;
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public String getPainter(){
        return this.painter;
    }
}
