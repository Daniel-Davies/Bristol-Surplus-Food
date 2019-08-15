package models;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@DiscriminatorValue(value = "Deliverer")
public class Deliverer extends User {

    @OneToMany (mappedBy = "deliverer")
    private List<Delivery> deliveries;
}
