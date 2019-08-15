package models;


import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.OrderBy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@DiscriminatorValue(value = "Beneficiary")
public class Beneficiary extends User {

    @OneToMany(mappedBy = "beneficiary")
    @JsonBackReference
    @OrderBy(clause = "availableTimeStart DESC")
    private List<FoodRequest> requests;

    @JsonIgnore
    public List<FoodRequest> getFoodRequests() {
        return requests;
    }

    public List<FoodRequest> getPending() {
        return FoodCollection.getPending(getFoodRequests());
    }

    public List<FoodRequest> getPrevious() {
        return FoodCollection.getPrevious(getFoodRequests());
    }
}
