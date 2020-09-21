package apiTest;


import apiTest.json.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class UserLogin {

    private final String AUTH_HEADER_NAME = "Access-Token";
    private final String LOGIN_PATH = "http://localhost:8080/api/user/login";

    private HttpPost postRequest;
    private final CloseableHttpClient client = HttpClientBuilder.create().build();
    private HttpResponse response;

    public HttpResponse login(String userEmail, String userPassword) throws IOException {

        postRequest = new HttpPost(LOGIN_PATH);
        postRequest.setHeader("Content-type", "application/json");

        User loginUser = new User(userEmail, userPassword);

        StringEntity entity = new StringEntity(loginUser.toJson());

        postRequest.setEntity(entity);
        response = client.execute(postRequest);

        return response;
    }
}
