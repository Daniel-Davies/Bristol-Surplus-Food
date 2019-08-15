package models;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Deliveries")
public class Delivery extends Model {

    @OneToOne(mappedBy = "comingDelivery", targetEntity = FoodCollection.class)
    @JsonBackReference
    private FoodAvailability foodAvailability;

    @OneToMany(mappedBy = "comingDelivery", targetEntity = FoodCollection.class)
    @JsonBackReference
    private List<FoodRequest> foodRequests;

    @ManyToOne
    @JoinColumn(name = "delivererId")
    private Deliverer deliverer;

 

    public FoodAvailability getFoodAvailability() {
        return foodAvailability;
    }

    public List<FoodRequest> getFoodRequests() {
        return foodRequests;
    }

    public Deliverer getDeliverer() {
        return deliverer;
    }


    public void setFoodRequests(List<FoodRequest> foodRequests) {
        this.foodRequests = foodRequests;
    }

    public void setFoodAvailability(FoodAvailability foodAvailability) {
        this.foodAvailability = foodAvailability;
    }

    public void setDeliverer(Deliverer deliverer) {
        this.deliverer = deliverer;
    }
}
