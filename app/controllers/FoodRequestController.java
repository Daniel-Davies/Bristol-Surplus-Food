package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.CurrentUser;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FoodRequestController extends Controller {

    @Inject
    private FormFactory formFactory;
    @Inject
    private JPAFoodRequestRepository repo;
    @Inject
    private CurrentUser currentUser;


    @Transactional
    public Result index() {
        if (!currentUser.get().getType().equals("Beneficiary")){
            return  badRequest("Only Beneficiaries can access this page");
        }
        Beneficiary b = (Beneficiary) currentUser.get();
        return ok(views.html.beneficiaries.history.render(b));
    }

    @Transactional
    public Result index_json(){
        if(!currentUser.get().getType().equals("Deliverer")){
            return  badRequest("Only Deliverers can access this page");
        }
        List<FoodRequest> foodrequests = repo.listAllWithoutComingDelivery();
        return ok(Json.toJson(foodrequests));
    }

    @Transactional
    public Result _new() {
        if(!currentUser.get().getType().equals("Beneficiary")){
            return  badRequest("Only Beneficiaries can access this page");
        }
        Beneficiary b = (Beneficiary) currentUser.get();
        Form<FoodRequest> form = formFactory.form(FoodRequest.class);
        return ok(views.html.foodrequest.create.render(b, form));
    }



    @Transactional
    public Result create() {
        if(!currentUser.get().getType().equals("Beneficiary")){
            return  badRequest("Only Beneficiaries can access this page");
        }

        Beneficiary beneficiary = (Beneficiary) currentUser.get();

        Form<FoodRequest> foodRequestForm = formFactory
                .form(FoodRequest.class)
                .bindFromRequest("availableTimeStart", "availableTimeEnd");

        if (foodRequestForm.hasErrors()) {
            return badRequest(views.html.foodrequest.create.render(beneficiary, foodRequestForm));
        }

        FoodRequest fr = foodRequestForm.get();
        fr.setBeneficiary(beneficiary);

        repo.insert(fr);

        return redirect(routes.FoodItemController.form(fr.getId()));
    }


    @Transactional
    public Result show(int id) {
        FoodRequest fr;
        try {
            fr = repo.findById(id);
        }
        catch (Exception e) {
            return notFound(views.html.defaultpages.notFound.render("get", "This food request was not found."));
        }
        return ok(views.html.foodrequest.show.render((Beneficiary) currentUser.get(), fr));

    }
}