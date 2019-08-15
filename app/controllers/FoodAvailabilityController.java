package controllers;

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
import play.mvc.Http;
import play.mvc.Result;
import services.CurrentUser;
import views.html.*;

import java.beans.Transient;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FoodAvailabilityController extends Controller {

    @Inject
    private FormFactory formFactory;
    @Inject
    private JPAFoodAvailabilityRepository repo;
    @Inject
    private CurrentUser currentUser;


    @Transactional
    public Result index() {
        if (!currentUser.get().getType().equals("Supplier")){
            return  badRequest("Only Suppliers can access this page");
        }
        Supplier s = (Supplier) currentUser.get();
        return ok(views.html.supplier.history.render(s));
    }

    @Transactional
    public Result index_json(){
        if(!currentUser.get().getType().equals("Deliverer")){
            return  badRequest("Only Deliverers can access this page");
        }
        List<FoodAvailability> foodAvailabilities = repo.listAllWithoutComingDelivery();
        return ok(Json.toJson(foodAvailabilities));
    }

    @Transactional
    public Result _new() {
        if(!currentUser.get().getType().equals("Supplier")){
            return  badRequest("Only Suppliers can access this page");
        }
        Supplier s = (Supplier) currentUser.get();
        Form<FoodAvailability> form = formFactory.form(FoodAvailability.class);
        return ok(views.html.supplier.makeAvailable.render(s, form));
    }


    @Transactional
    public Result create() {
        if(!currentUser.get().getType().equals("Supplier")){
            return  badRequest("Only Suppliers can access this page");
        }

        Supplier supplier = (Supplier) currentUser.get();

        Form<FoodAvailability> foodAvailabilityForm = formFactory
                .form(FoodAvailability.class)
                .bindFromRequest("availableTimeStart", "availableTimeEnd");

        if (foodAvailabilityForm.hasErrors()) {
            return badRequest(views.html.supplier.makeAvailable.render(supplier, foodAvailabilityForm));
        }

        FoodAvailability fa = foodAvailabilityForm.get();
        fa.setSupplier(supplier);

        repo.insert(fa);

        return redirect(routes.FoodItemController.form(fa.getId()));
    }

    @Transactional
    public Result show(int id) {
        FoodAvailability fa = repo.findById(id);
        return ok(views.html.foodavailability.show.render((Supplier) currentUser.get(), fa));
    }


}