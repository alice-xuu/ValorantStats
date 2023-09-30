package valorantstats.model.apicalls;

/** Represents an HTTP response containing the statusCode and body */
public class ApiResponse {
  int statusCode;
  String body;

  public ApiResponse(int statusCode, String body) {
    this.statusCode = statusCode;
    this.body = body;
  }

  /**
   *
   * @return response status code, generally 200 indicates a successful request, >= 400 indicates an error from the client or server
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   *
   * @return the content of the request
   */
  public String getBody() {
    return body;
  }
}
