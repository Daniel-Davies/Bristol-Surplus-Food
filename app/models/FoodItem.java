package models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MetaValue;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "FoodItems")
public class FoodItem extends Model {

    private double amount;

    @Enumerated
    @Column(columnDefinition = "int")
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "foodCategoryId")
    private FoodCategory category;

    @ManyToOne
    @JoinColumn(name = "foodCollectionId", nullable = false)
    @JsonBackReference
    private FoodCollection collection;


    public double getAmount() {
        return amount;
    }
    public FoodCategory getCategory() {
        return category;
    }
    public FoodCollection getCollection() {
        return collection;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public void setCollection(FoodCollection collection) {
        this.collection = collection;
    }



    public static enum Unit {
        KILO, LITER, PACK, OTHER;
    }
}
