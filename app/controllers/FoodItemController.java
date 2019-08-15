package controllers;

import com.google.inject.Inject;
import com.sun.org.apache.bcel.internal.generic.Select;
import models.*;
import play.db.jpa.Transactional;
import play.mvc.*;
import services.CurrentUser;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import services.CurrentUser;
import views.html.*;

import java.beans.Transient;
import java.util.List;

public class FoodItemController extends Controller {
    @Inject FormFactory formFactory;

    @Inject
    JPAFoodAvailabilityRepository faRepo;

    @Inject
    JPAFoodRequestRepository frRepo;

    @Inject
    JPAFoodItemRepository fiRepo;

    @Inject
    JPARepository<FoodCategory> jpaRepo;

    @Inject
    CurrentUser currentUser;

    @Transactional
    public Result form(int fcId) {

        User u = currentUser.get();
        FoodCollection fc = foodCollection(u, fcId);

        Form<FoodItem> form = formFactory.form(FoodItem.class);
        return ok(views.html.fooditem.form.render(u, fc, form, categories(), collectionRoute(u, fcId)));
    }

    @Transactional
    public Result create(int fcId) {

            User u = currentUser.get();
            FoodCollection fc = foodCollection(u, fcId);

            Form<FoodItem> foodItemForm = formFactory
                    .form(FoodItem.class)
                    .bindFromRequest("amount", "unit");

            if (foodItemForm.hasErrors()) {
                return badRequest(views.html.fooditem.form.render(u, fc, foodItemForm, categories(), collectionRoute(u, fcId)));
            }

            int categoryId = Integer.parseInt(foodItemForm.field("categoryid").getValue().get());
            FoodCategory fcat = jpaRepo.findById(FoodCategory.class, categoryId);

            FoodItem fi = foodItemForm.get();
            fi.setCategory(fcat);
            fi.setCollection(fc);

            fiRepo.insert(fi);

            return redirect(collectionRoute(u, fcId));
    }

    private List<FoodCategory> categories() {
        return jpaRepo.listAll(FoodCategory.class);
    }

    private FoodCollection foodCollection(User u, int fcId) {
        switch (u.getType()) {
            case "Supplier": return faRepo.findById(fcId);
            case "Beneficiary": return frRepo.findById(fcId);
        }
        return null;
    }

    private Call collectionRoute(User u, int fcId) {
        switch (u.getType()) {
            case "Supplier": return routes.FoodAvailabilityController.show(fcId);
            case "Beneficiary": return routes.FoodRequestController.show(fcId);
        }
        return null;
    }
}
