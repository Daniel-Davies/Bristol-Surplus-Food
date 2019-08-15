package models;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;


public class JPAFoodAvailabilityRepository extends JPARepository<FoodAvailability> {

    public FoodAvailability findById(int id){
    return super.findById(FoodAvailability.class, id);
    }

    public int updateAvailableTimeStart(int id, Date newTime){
        return update(FoodAvailability.class, id, "availableTimeStart", newTime);
    }

    public int updateAvailableTimeEnd(int id, Date newTime){
        return update(FoodAvailability.class, id, "availableTimeEnd", newTime);
    }

    public int updateComingDelivery(int id, Delivery newComingDelivery){
        return update(FoodAvailability.class, id, "comingDelivery", newComingDelivery);
    }

    public  List<FoodAvailability> listAllWithoutComingDelivery() {

        // prelude to load safe selectors
        CriteriaBuilder b = jpaApi.em().getCriteriaBuilder();
        CriteriaQuery<FoodAvailability> query = b.createQuery(FoodAvailability.class);
        Root<FoodAvailability> root = query.from(FoodAvailability.class);
        query.where(b.isNull(root.get("comingDelivery")));

        // corresponds to SELECT * FROM tClassTable ORDER BY orderAttributeName;
        query.select(root);
        List<FoodAvailability> result = jpaApi.em().createQuery(query).getResultList();
        return result;
    }
}