package valorantstats.model.apicalls;

import com.google.gson.Gson;
import valorantstats.model.apicalls.dummy.OutputDummyApi;
import valorantstats.model.jsonobjects.PostImgurImage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** Sends requests to a real imgur api when the output Api is specified as online */
public class OutputApiCallsOnline implements OutputApiCalls {

  public OutputApiCallsOnline() {}

  @Override
  public ApiResponse postRequestImgurShortForm(String img) {
    try {

      String uri = "https://api.imgur.com/3/upload";
      PostImgurImage post = new PostImgurImage(img, "base64");
      Gson gson = new Gson();
      String postJSON = gson.toJson(post);
      HttpRequest request =
          HttpRequest.newBuilder(new URI(uri))
              .header("Authorization", "Client-ID " + System.getenv("IMGUR_API_KEY"))
              .header("Content-Type", "application/json")
              .POST(HttpRequest.BodyPublishers.ofString(postJSON))
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

  @Override
  public void setOutputDummyApi(OutputDummyApi outputDummyApi) {}
}
