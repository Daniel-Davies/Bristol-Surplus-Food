package models;

import play.db.jpa.JPAApi;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class JPAFoodRequestRepository extends JPARepository<FoodRequest> {

    public FoodRequest findById(int id){
        return super.findById(FoodRequest.class, id);
    }

    public int updateComingDelivery(int id, Delivery newComingDelivery){
        return update(FoodRequest.class, id, "comingDelivery", newComingDelivery);
    }

    public int updateAvailableTimeStart(int id, Date newTime){
        return update(FoodRequest.class, id, "availableTimeStart", newTime);
    }

    public int updateAvailableTimeEnd(int id, Date newTime){
        return update(FoodRequest.class, id, "availableTimeEnd", newTime);
    }

    public List<FoodRequest> listAll() {
        return super.listAll(FoodRequest.class);
    }

    public  List<FoodRequest> listAllWithoutComingDelivery() {

        // prelude to load safe selectors
        CriteriaBuilder b = jpaApi.em().getCriteriaBuilder();
        CriteriaQuery<FoodRequest> query = b.createQuery(FoodRequest.class);
        Root<FoodRequest> root = query.from(FoodRequest.class);
        query.where(b.isNull(root.get("comingDelivery")));

        // corresponds to SELECT * FROM tClassTable ORDER BY orderAttributeName;
        query.select(root);
        List<FoodRequest> result = jpaApi.em().createQuery(query).getResultList();
        return result;
    }
}
