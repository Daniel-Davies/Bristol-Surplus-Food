package models;

import play.db.jpa.JPAApi;


public class JPAFoodItemRepository extends JPARepository<FoodItem> {

    public FoodItem findById(int id){
        return super.findById(FoodItem.class, id);
    }

    public int updateAmount(int id, int newAmount){
        return update(FoodItem.class, id, "amount", newAmount);
    }

}
