package Checkfile;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class authenticate_ae {

    public static void main(String[] args) throws IOException {
        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create a request body for the username and password
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", "Balaji_Giri")
                .addFormDataPart("password", "Edge@1234")
                .build();

        // Create a request object with the URL and request body
        Request request = new Request.Builder()
                .url("http://localhost:8080/aeengine/rest/authenticate")
                .post(requestBody)
                .build();

        // Send the request and get the response
        Response response = client.newCall(request).execute();



				// Get the authentication token from the response body
				String responseBody = response.body().string();
				String authToken = extractAuthToken(responseBody);

				System.out.println("Authentication token: " + authToken);
	}

	private static String extractAuthToken(String responseBody) {
		// TODO Auto-generated method stub
		  return "my_auth_token";
	}

}
