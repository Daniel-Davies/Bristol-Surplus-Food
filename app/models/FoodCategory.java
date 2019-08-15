package models;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FoodCategories")
public class FoodCategory extends Model {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }
}
