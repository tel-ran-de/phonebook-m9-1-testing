package apiTest;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Pb33Test {
    UserLogin userLogin = new UserLogin();

    private final String ENDPOINT = "http://localhost:8080/api/user/auth-password";
    private HttpPut putRequest;
    private HttpResponse response;
    private final CloseableHttpClient client = HttpClientBuilder.create().build();

    private final String USER_EMAIL = "phone.boock.test@gmail.com";
    private final String USER_PASS = "phone.boock.test";

    private Header authHeader;


    @Before
    public void init() throws IOException {
        response = userLogin.login(USER_EMAIL, USER_PASS);
        authHeader = response.getFirstHeader("Access-Token");
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void tesChangePass_resultOK() throws IOException {
        String newPass = "0123456789";

        putRequest = new HttpPut(ENDPOINT);

        putRequest.setHeader("Content-type", "application/json");
        putRequest.setHeader(authHeader);

        StringEntity entity = new StringEntity("{\"password\"" + ":" + "\"" + newPass + "\"}");

        putRequest.setEntity(entity);
        response = client.execute(putRequest);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

        int statusLogin = userLogin.login(USER_EMAIL, USER_PASS).getStatusLine().getStatusCode();
        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusLogin);

        statusLogin = userLogin.login(USER_EMAIL, newPass).getStatusLine().getStatusCode();
        assertEquals(HttpStatus.SC_OK, statusLogin);
    }

    @Test
    public void tesChangePass_passToShort_result404() throws IOException {
        String newPass = "1234";

        putRequest = new HttpPut(ENDPOINT);

        putRequest.setHeader("Content-type", "application/json");
        putRequest.setHeader(authHeader);

        StringEntity entity = new StringEntity("{\"password\"" + ":" + "\"" + newPass + "\"}");

        putRequest.setEntity(entity);
        response = client.execute(putRequest);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

    @Test
    public void tesChangePass_passTooLong_result404() throws IOException {
        String newPass = "0123456789_0123456789";

        putRequest = new HttpPut(ENDPOINT);

        putRequest.setHeader("Content-type", "application/json");
        putRequest.setHeader(authHeader);

        StringEntity entity = new StringEntity("{\"password\"" + ":" + "\"" + newPass + "\"}");

        putRequest.setEntity(entity);
        response = client.execute(putRequest);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

    @Test
    public void tesChangePass_userUnauthorized_result401() throws IOException {
        String newPass = "0123456789";

        putRequest = new HttpPut(ENDPOINT);

        putRequest.setHeader("Content-type", "application/json");

        StringEntity entity = new StringEntity("{\"password\"" + ":" + "\"" + newPass + "\"}");

        putRequest.setEntity(entity);
        response = client.execute(putRequest);

        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());
    }


    @After
    public void resetPass() throws IOException {
        putRequest = new HttpPut(ENDPOINT);

        putRequest.setHeader("Content-type", "application/json");
        putRequest.setHeader(authHeader);

        StringEntity entity = new StringEntity("{\"password\"" + ":" + "\"" + USER_PASS + "\"}");

        putRequest.setEntity(entity);
        response = client.execute(putRequest);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }
}
