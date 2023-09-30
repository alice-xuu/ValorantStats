package valorantstats.model.apicalls;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** Sends requests to Wit.Ai api and returns an ApiResponse object */
public class WitAiApiCallsImpl implements WitAiApiCalls {

  String witClientKey;
  String witServerKey;

  public WitAiApiCallsImpl() {
    this.witClientKey = System.getenv("WIT_CLIENT_KEY");
    this.witServerKey = System.getenv("WIT_SERVER_KEY");
  }

  @Override
  public ApiResponse getRequestMessage(String input) {
    try {
      String formatted = input.replaceAll("\\s+", "%20");

      String uri = "https://api.wit.ai/message?v=20220503&q=" + formatted;

      HttpRequest request =
          HttpRequest.newBuilder(new URI(uri))
              .GET()
              .header("Accept", "application/json")
              .header("Authorization", "Bearer " + witServerKey)
              .build();

      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      ApiResponse apiResponse = new ApiResponse(response.statusCode(), response.body());

      return apiResponse;

    } catch (IOException | InterruptedException e) {
      System.out.println(e.getMessage());
    } catch (URISyntaxException ignored) {
    }
    return null;
  }
}
