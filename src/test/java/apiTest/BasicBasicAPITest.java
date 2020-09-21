package apiTest;

import apiTest.json.ContactJson;
import apiTest.json.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicBasicAPITest {

    private HttpPost postRequest;
    private HttpPut putRequest;
    private HttpGet getRequest;
    private HttpDelete deleteRequest;

    private HttpResponse response;
    private final CloseableHttpClient client = HttpClientBuilder.create().build();

    private final String userEmail = "phone.boock.test@gmail.com";
    private final String userPassword = "phone.boock.test";
    private static String token;

    private final String baseUrl = "http://localhost:8080/api";
    private final String contactUrl = baseUrl + "/contact";
    private final String loginUrl = baseUrl + "/user/login";
    private final String registerUrl = baseUrl + "/user";

    List<ContactJson> contactJsons;
    private StringBuilder stringBuilder;

    @Before
    public void init() {
    }

    @Test
    public void tes001_createNewUser() throws IOException {
        postRequest = new HttpPost(registerUrl);
        postRequest.setHeader("Content-type", "application/json");

        User loginUser = new User("newUser1@gmail.com", userPassword);

        StringEntity entity = new StringEntity(loginUser.toJson());

        postRequest.setEntity(entity);
        response = client.execute(postRequest);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void test002_UserAlreadyExist() throws IOException {
        postRequest = new HttpPost(registerUrl);
        postRequest.setHeader("Content-type", "application/json");

        User loginUser = new User("newUser1@gmail.com", userPassword);

        StringEntity entity = new StringEntity(loginUser.toJson());

        postRequest.setEntity(entity);
        response = client.execute(postRequest);

        HttpEntity responseEntity = response.getEntity();
        String responseString = EntityUtils.toString(responseEntity, "UTF-8");

        assertEquals("{\"message\":\"Error! User already exists\"}", responseString);
        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

    @Test
    public void test003_testLogin() throws IOException {
        postRequest = new HttpPost(loginUrl);
        postRequest.setHeader("Content-type", "application/json");

        User loginUser = new User(userEmail, userPassword);

        StringEntity entity = new StringEntity(loginUser.toJson());

        postRequest.setEntity(entity);
        response = client.execute(postRequest);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        token = response.getFirstHeader("Access-Token").getValue();
    }

    @Test
    public void test004_loopCreateContact() throws IOException {
        fillContactArray();
        for (ContactJson contactJson : contactJsons)
            addContacts(contactJson);
    }

    private void addContacts(ContactJson contactJson) throws IOException {
        postRequest = new HttpPost(contactUrl);
        postRequest.setHeader("Access-Token", token);
        postRequest.setHeader("Content-type", "application/json");

        StringEntity entity = new StringEntity(contactJson.toString());
        postRequest.setEntity(entity);
        response = client.execute(postRequest);

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }

    private void fillContactArray() {
        contactJsons = Arrays.asList(
                new ContactJson("Eduard", "Schmidt", null),
                new ContactJson("Max", "Mustermann", null),
                new ContactJson("Anna", "Klein", null),
                new ContactJson("Maria", "Bauer", null),
                new ContactJson("Martin", "Kenner", null),
                new ContactJson("Denis", "Fuhrmann", null),
                new ContactJson("Vasilij", "Popov", null),
                new ContactJson("Swetlana", "Berger", null),
                new ContactJson("Peter", "Schmidt", null),
                new ContactJson("Daniela", "Lutzenberger", null),
                new ContactJson("Emily-Sofie", "Smith", null),
                new ContactJson("Isabel", "Tailor", null),
                new ContactJson("Markus", "Taylor", null),
                new ContactJson("Florian", "Thomas", null),
                new ContactJson("Mercedes", "Benz", null),
                new ContactJson("Karl", "Mueller", null)
        );
    }
}
