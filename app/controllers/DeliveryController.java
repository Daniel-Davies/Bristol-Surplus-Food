package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import models.*;
import play.api.libs.json.Json;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.CurrentUser;
import views.html.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import scala.Int;

public class DeliveryController extends Controller {
    @Inject
    private JPAFoodRequestRepository foodRequestRepo;

    @Inject
    private JPAFoodAvailabilityRepository foodAvailabilityRepo;

    @Inject
    private JPADeliveryRepository deliveryRepo;

    @Inject
    private CurrentUser currentUser;

    @Transactional
    public Result create(){
        if(!currentUser.get().getType().equals("Deliverer")){
            return  badRequest("Only Deliverers can create new deliveries\n");
        }

        JsonNode reqBody = request().body().asJson();
        if (reqBody == null) {
            return badRequest("No Json body. Expected: {\"faId\": id, \"frIds\": [ids]}\n");
        }

        int faId = reqBody.findPath("faId").asInt();
        if (faId == 0) {
            return badRequest("No foodAvailability selected. Json body expected :  {\"faId\": id, \"frIds\": [ids]}\n");

        }

        List<JsonNode> frNodes = Lists.newArrayList(reqBody.findPath("frIds").elements());
        if (frNodes.isEmpty()) {
            return badRequest("No food request selected. Json body expected :  {\"faId\": id, \"frIds\": [ids]}\n");
        }


        List<FoodRequest> foodRequests = new ArrayList();

        FoodAvailability fa;
        try {
            fa = (FoodAvailability) foodAvailabilityRepo.findById(faId);
            if (fa.getComingDelivery() != null) {
                return badRequest("The FoodAvailability " + fa.getId() + " has already been selected by a deliverer.");
            }
            int frId;
            FoodRequest fr;

            for (JsonNode node : frNodes) {
                frId = node.asInt();
                fr = (FoodRequest) foodRequestRepo.findById(frId);
                if (fr.getComingDelivery() != null) {
                    return badRequest("The FoodRequest " + fr.getId() + " has already been selected by a deliverer.");
                }
                foodRequests.add(fr);
            }
        } catch (EntityNotFoundException e) {
            return badRequest(e.getMessage());
        }


        Delivery d = new Delivery();

        d.setFoodAvailability(fa);
        d.setDeliverer((Deliverer) currentUser.get());
        d.setFoodRequests(foodRequests);

        deliveryRepo.insert(d);

        for (FoodRequest fr : foodRequests) {
            foodRequestRepo.updateComingDelivery(fr.getId(), d);
        }
        foodAvailabilityRepo.updateComingDelivery(fa.getId(), d);

        return ok();
    }
}