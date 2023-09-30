package valorantstats.model.apicalls;

/** Sends requests to Wit.Ai api and returns an ApiResponse object */
public interface WitAiApiCalls {

  /**
   * Return the meaning of a sentence from Wit.Ai through a get request
   *
   * @param input some sentence or word
   * @return ApiResponse containing the response status code and body content
   */
  ApiResponse getRequestMessage(String input);
}
