package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "FoodAvailability")
public class FoodAvailability extends FoodCollection {


    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonManagedReference
    private Supplier supplier;


    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier s) {
        if (s != null) this.supplier = s;
    }
}
