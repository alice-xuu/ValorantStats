package valorantstats.model.apicalls;

import valorantstats.model.apicalls.dummy.InputDummyApi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** Sends requests to the real Pandascore api when the inputApi is specified as online */
public class InputApiCallsOnline implements InputApiCalls {

  public InputApiCallsOnline() {}

  @Override
  public ApiResponse getRequestTeam(String name) {
    try {
      String formatted = name.replaceAll("\\s+", "%20");
      String uri =
          "https://api.pandascore.co/valorant/teams?filter[name]="
              + formatted
              + "&sort=&page=1&per_page=50";
      HttpRequest request =
          HttpRequest.newBuilder(new URI(uri))
              .GET()
              .header("Accept", "application/json")
              .header("Authorization", "Bearer " + System.getenv("INPUT_API_KEY"))
              .build();

      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      ApiResponse apiResponse = new ApiResponse(response.statusCode(), response.body());
      return apiResponse;

    } catch (IOException | InterruptedException | URISyntaxException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public ApiResponse getRequestTeamUpcomingMatches(String name) {
    try {
      String formatted = name.replaceAll("\\s+", "%20");
      String uri =
          "https://api.pandascore.co/valorant/matches/upcoming?filter[opponent_id]="
              + formatted
              + "&sort=&page=1&per_page=50";

      HttpRequest request =
          HttpRequest.newBuilder(new URI(uri))
              .GET()
              .header("Accept", "application/json")
              .header("Authorization", "Bearer " + System.getenv("INPUT_API_KEY"))
              .build();

      HttpClient client = HttpClient.newBuilder().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      ApiResponse apiResponse = new ApiResponse(response.statusCode(), response.body());
      return apiResponse;

    } catch (IOException | InterruptedException | URISyntaxException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public void setInputDummyApi(InputDummyApi inputDummyApi) {}
}
