package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;
import play.data.format.Formats;
import play.libs.F;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "FoodCollections")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public class FoodCollection extends Model {

    @OneToMany(mappedBy = "collection")
    @JsonManagedReference
    private List<FoodItem> items;

    @Temporal(TemporalType.TIMESTAMP)
    @Formats.DateTime(pattern = "MM/dd/yyyy hh:mm a")
    private Date availableTimeStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Formats.DateTime(pattern = "MM/dd/yyyy hh:mm a")
    private Date availableTimeEnd;

    @ManyToOne
    @JoinColumn(name = "comingDeliveryId")
    @JsonManagedReference
    private Delivery comingDelivery;


    public Date getAvailableTimeStart() {
        return availableTimeStart;
    }

    public Date getAvailableTimeEnd() {
        return availableTimeEnd;
    }

    public boolean isStillAvailable() {

        return this.getAvailableTimeEnd().after(new Date());
    }

    public void setAvailableTimeStart(Date availableTimeStart) {
        this.availableTimeStart = availableTimeStart;
    }

    public void setAvailableTimeEnd(Date availableTimeEnd) {
        this.availableTimeEnd = availableTimeEnd;
    }

    public List<FoodItem> getItems() {
        return this.items;
    }

    public Delivery getComingDelivery() {
        return comingDelivery;
    }

    public void setComingDelivery(Delivery comingDelivery) {
        this.comingDelivery = comingDelivery;
    }

    public static <T extends FoodCollection> List<T> getPending(List<T> list) {
        return list.stream()
                .filter(fc -> fc.isStillAvailable())
                .collect(Collectors.toList());
    }

    public static <T extends FoodCollection> List<T> getPrevious(List<T> list) {
        return list.stream()
                .filter(fc -> !fc.isStillAvailable())
                .collect(Collectors.toList());
    }
}
