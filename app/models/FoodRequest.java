package models;


import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "FoodRequest")
public class FoodRequest extends FoodCollection {

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonManagedReference
    private Beneficiary beneficiary;

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }
}
