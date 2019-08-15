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

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

public class DelivererTest {

    Application fakeAppWithMemoryDb = fakeApplication(inMemoryDatabase("efficace_db_dev"));

    @Test
    public void listFoodAvailabilitiesTest() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/app/foodavailability?token=eyJhbGciOiJIUzI1NiJ9.eyIkaW50X3Blcm1zIjpbXSwic3ViIjoib3JnLnBhYzRqLnNxbC5wcm9maWxlLkRiUHJvZmlsZSNkZWxpdmVyZXJAaG90bWFpbC5jb20iLCJhZGRyZXNzIjoibmV3YWRkcnIiLCJuYW1lIjoibmV3bmFtZSIsIiRpbnRfcm9sZXMiOltdLCJ0eXBlIjoiRGVsaXZlcmVyIn0.E1E5GC6ooNRzNnIUpitl-hTpU6Vj-Huujv6Hac86Tco");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.body().contentType().get());
    }




    @Test
    public void listFoodRequestsTest() {
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/app/foodrequest?token=eyJhbGciOiJIUzI1NiJ9.eyIkaW50X3Blcm1zIjpbXSwic3ViIjoib3JnLnBhYzRqLnNxbC5wcm9maWxlLkRiUHJvZmlsZSNkZWxpdmVyZXJAaG90bWFpbC5jb20iLCJhZGRyZXNzIjoibmV3YWRkcnIiLCJuYW1lIjoibmV3bmFtZSIsIiRpbnRfcm9sZXMiOltdLCJ0eXBlIjoiRGVsaXZlcmVyIn0.E1E5GC6ooNRzNnIUpitl-hTpU6Vj-Huujv6Hac86Tco");
        Result result = route(fakeAppWithMemoryDb, request);

        assertEquals(OK, result.status());
        assertEquals("application/json", result.body().contentType().get());
    }


    @Test
    public void createDeliveryTest() {
        Map<String,String> attributes = new HashMap<>();
        attributes.put("faId", "1");
        attributes.put("frIds", "[2]");

        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(POST).header("content-type", "application/json").bodyForm(attributes)
                .uri("http://localhost:9000/app/delivery?token=eyJhbGciOiJIUzI1NiJ9.eyIkaW50X3Blcm1zIjpbXSwic3ViIjoib3JnLnBhYzRqLnNxbC5wcm9maWxlLkRiUHJvZmlsZSNkZWxpdmVyZXJAaG90bWFpbC5jb20iLCJhZGRyZXNzIjoibmV3YWRkcnIiLCJuYW1lIjoibmV3bmFtZSIsIiRpbnRfcm9sZXMiOltdLCJ0eXBlIjoiRGVsaXZlcmVyIn0.E1E5GC6ooNRzNnIUpitl-hTpU6Vj-Huujv6Hac86Tco");
        Result result = route(fakeAppWithMemoryDb, request);


        assertEquals(BAD_REQUEST, result.status());
    }


    @Test
    public void showFrs(){
        Http.RequestBuilder request = Helpers.fakeRequest()
                .method(GET)
                .uri("http://localhost:9000/foodrequest?token=eyJhbGciOiJIUzI1NiJ9.eyIkaW50X3Blcm1zIjpbXSwic3ViIjoib3JnLnBhYzRqLnNxbC5wcm9maWxlLkRiUHJvZmlsZSNkZWxpdmVyZXJAaG90bWFpbC5jb20iLCJhZGRyZXNzIjoibmV3YWRkcnIiLCJuYW1lIjoibmV3bmFtZSIsIiRpbnRfcm9sZXMiOltdLCJ0eXBlIjoiRGVsaXZlcmVyIn0.E1E5GC6ooNRzNnIUpitl-hTpU6Vj-Huujv6Hac86Tco");
        Result result = route(fakeAppWithMemoryDb, request);

        // Expected: Bad Request, only Suppliers can access this route
        assertEquals(BAD_REQUEST, result.status());

    }

}
