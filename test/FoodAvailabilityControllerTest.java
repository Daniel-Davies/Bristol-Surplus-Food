import controllers.FoodAvailabilityController;
import models.JPAFoodAvailabilityRepository;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Http.RequestBuilder;
import play.Application;
import play.libs.Json;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.*;
import play.libs.Json;
import play.db.jpa.Transactional;
import services.CurrentUser;
import controllers.routes;
import play.mvc.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import play.Application;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import sun.net.www.http.HttpClient;
import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import com.google.inject.Inject;
import models.JPAUserRepository;
import models.User;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import org.pac4j.sql.profile.DbProfile;
import play.db.jpa.Transactional;

import static play.mvc.Controller.ctx;

import controllers.HomeController;

import org.junit.Test;

import play.mvc.Result;
import play.twirl.api.Content;

import static org.mockito.Mockito.*;

import com.google.inject.Inject;
import models.JPAUserRepository;
import models.User;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import org.pac4j.sql.profile.DbProfile;
import play.db.jpa.Transactional;

import static play.mvc.Controller.ctx;



public class FoodAvailabilityControllerTest {

    Application fakeAppWithMemoryDb = fakeApplication(inMemoryDatabase("efficace_db_dev"));

    @Inject
    private CurrentUser currentUser;
    //////////////////TRANSPARENT TESTING/////////////////////////////
    @Test
    public void enterValidAvailability() {
        Map<String,Date> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("availableTimeStart", new Date());
        attributes.put("availableTimeStart", new Date());

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Enter a food item"));
    }

    @Test
    public void enterValidFoodItem() {
        Map<String,String> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("quantity", "KILO");
        attributes.put("category", "Fruit");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Enter a food item"));
    }

    /////////////////////////////////////////OPAQUE/////////////////////////////////////////////////


    ///FA///

    @Test
    public void enterAvailabilityInPast() {
        Date pastDate = new Date();
        pastDate.setTime(1);
        Map<String,Date> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("availableTimeStart", pastDate);
        attributes.put("availableTimeStart", new Date());

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(400, result.status());
    }

    @Test
    public void enterAvailabilityFromDateAfterToDate() {
        Date futureDay = new Date();
        futureDay.setTime(1524711046090);
        Map<String,Date> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("availableTimeStart", futureDay);
        attributes.put("availableTimeStart", new Date());

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(400, result.status());
    }

    @Test
    public void enterAvailabilityNullDates() {
        Map<String,Date> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("availableTimeStart", null);
        attributes.put("availableTimeStart", null);

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(400, result.status());
    }

    //Food Item//

    @Test
    public void invalidUnitEntryForFoodItem() {
        Map<String,String> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("quantity", "LBS");
        attributes.put("category", "fruit");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Enter a food item"));
    }

    @Test
    public void enterUnreasonableQuantityForFoodItem() {
        Map<String,String> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("quantity", "-1");
        attributes.put("category", "anything");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Enter a food item"));
    }

    @Test
    public void enterNullFoodItem() {
        Map<String,String> attributes = new HashMap<>();
        attributes.put("faId", null);
        attributes.put("quantity", null);
        attributes.put("category", null);

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Enter a food item"));
    }

    //////////////////////////////////////////////////ADDITIONS//////////////////////////////////////////

    @Test
    public void enterAvailabilityWrongFormat() {
        Map<String,String> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("availableTimeStart", "01001");
        attributes.put("availableTimeStart", "0001");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).bodyForm(attributes)
                .uri("http://localhost:9000/app/foodavailability");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(400, result.status());
    }
}
