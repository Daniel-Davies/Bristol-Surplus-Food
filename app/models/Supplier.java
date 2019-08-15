package models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.OrderBy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue(value = "Supplier")
public class Supplier extends User {

    @OneToMany(mappedBy = "supplier")
    @JsonBackReference
    @OrderBy(clause = "availableTimeStart DESC")
    private List<FoodAvailability> availabilities;


    public List<FoodAvailability> getAvailabilities() {
        return availabilities;
    }

    public List<FoodAvailability> getPending() {
        return FoodCollection.getPending(getAvailabilities());
    }

    public List<FoodAvailability> getPrevious() {
        return FoodCollection.getPrevious(getAvailabilities());
    }
}
