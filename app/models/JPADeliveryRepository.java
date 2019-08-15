package models;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;

public class JPADeliveryRepository extends JPARepository<Delivery> {

    public Delivery findById(int id){
        return super.findById(Delivery.class, id);
    }

}
