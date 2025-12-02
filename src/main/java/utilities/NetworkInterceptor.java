package utilities;

import com.google.gson.JsonObject;
import drivers.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v131.network.Network;
import org.openqa.selenium.devtools.v131.network.model.Request;
import org.openqa.selenium.devtools.v131.network.model.RequestId;
import org.openqa.selenium.devtools.v131.network.model.Response;


import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.google.gson.JsonParser.parseString;

public class NetworkInterceptor {
    public WebDriver driver;
    public DevTools devTools;

    public NetworkInterceptor(String specificUrl) {
        this.driver = DriverManager.getDriver();
        initializeDevTools();
    }

    private void initializeDevTools() {
        devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
    }

    public void startIntercepting() {

        // Listen for request events
        devTools.addListener(Network.requestWillBeSent(), rq -> {
            Request request = rq.getRequest();

            // Print intercepted request URL , method
            System.out.println("Intercepted URL: " + request.getUrl());
            System.out.println("Intercepted method: " + request.getMethod());

        });
        // Enable the Network domain to capture network traffic
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        // Store response bodies for the specific URL
        ConcurrentMap<RequestId, String> responseBodies = new ConcurrentHashMap<>();
        String specificUrl = "https://tempsit.hr.qeema.io/api/project/v2/timesheet/4/2024"; // Change this to your specific URL

        // Capture response bodies for the specific URL
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            RequestId requestId = responseReceived.getRequestId();
            Response response = responseReceived.getResponse();
            String url = response.getUrl();

            // Check if the response URL matches the specific URL
            if (url.equals(specificUrl)) {
                System.out.println("Captured Response URL: " + url);
                System.out.println("Captured Response Status: " + response.getStatus());

                // Fetch and store response body
                Network.GetResponseBodyResponse responseBody = devTools.send(Network.getResponseBody(requestId));
                String body = responseBody.getBody();
                responseBodies.put(requestId, body);
                System.out.println("Captured Response Body: " + body);

                // Parse the response body JSON using Gson's JsonParser
                JsonObject json = parseString(body).getAsJsonObject();
                // Extract the value of the 'totalElements' field
                int totalElements = json.get("totalElements").getAsInt();
                System.out.println("Total Elements: " + totalElements);
            }
        });
    }


    public void stopIntercepting() {
        // Remove the listener and clean up resources
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        devTools.close();
    }
}
