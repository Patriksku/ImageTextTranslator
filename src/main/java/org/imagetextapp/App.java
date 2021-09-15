package org.imagetextapp;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Flow;

/**
 * Hello world!
 *
 */
public class App {

    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";


    public static void main( String[] args ) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(POSTS_URL))
                .build();


        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//        System.out.println(response.body());

        // Parse JSON to object
            // ObjectMapper mapper = new ObjectMapper();
            // rest: https://youtu.be/5MmlRZZxTqk?t=1002
            // https://itnext.io/how-to-store-passwords-and-api-keys-in-project-code-1eaf5cb235c9
            // POST: https://zetcode.com/java/getpostrequest/

    }
}
