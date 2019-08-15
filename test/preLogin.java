import services.CurrentUser;
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



public class preLogin extends WithBrowser {
    @Inject
    private CurrentUser currentUser;

    protected TestBrowser provideBrowser(int port) {
        return Helpers.testBrowser(port);
    }

    @Test
    public void testBadRoute() {
        RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("/fedshfs");

        Result result = route(fakeAppWithMemoryDb, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    @Test
    public void testValidRoute() {
        Http.RequestBuilder requestt = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/");
        Result resultt = route(fakeAppWithMemoryDb, requestt);

        assertEquals(OK, resultt.status());
    }

    @Test
    public void noAccessRequests() {
        Http.RequestBuilder requesttt = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/foodrequest");
        Result resulttt = route(fakeAppWithMemoryDb, requesttt);

        // Expected: not authorized as not logged in
        assertEquals(303, resulttt.status());
    }

    @Test
    public void noAccessIndex() {
        Http.RequestBuilder requesttt = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/");
        Result result = route(fakeAppWithMemoryDb, requesttt);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("You are not logged in"));
    }

    @Test
    public void loginPage() {
        Http.RequestBuilder requesttt = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/login");
        Result result = route(fakeAppWithMemoryDb, requesttt);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Login"));
    }

    @Test
    public void register() {
        Http.RequestBuilder requesttt = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/login");
        Result result = route(fakeAppWithMemoryDb, requesttt);

        assertEquals(OK, result.status());
        assertTrue(contentAsString(result).contains("Register"));
    }


}
