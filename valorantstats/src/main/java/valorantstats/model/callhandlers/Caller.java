package valorantstats.model.callhandlers;

import com.google.gson.Gson;
import valorantstats.model.CacheManager;
import valorantstats.model.apicalls.ApiResponse;
import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.ErrorResponseInput;

import java.util.AbstractMap.SimpleEntry;

/**
 * Calls requests for data for a team name and match data for a team id
 */
public abstract class Caller {

  Caller nextCaller = null;
  CacheManager cacheManager;
  InputApiCalls inputApiCalls;

  public Caller(CacheManager cacheManager, InputApiCalls inputApiCalls) {
    this.cacheManager = cacheManager;
    this.inputApiCalls = inputApiCalls;
  }

  /**
   *
   * @param nextCaller caller responsible for processing the team name or id
   */
  public void setNextCaller(Caller nextCaller) {
    this.nextCaller = nextCaller;
  }

  /**
   * Request team information for a team name
   * @param name team name to be searched for
   * @param cacheOverride override/go past database or not
   * @return response result and body content
   */
  public abstract SimpleEntry<CallResult, String> callTeam(String name, boolean cacheOverride);

  /**
   * Request match information for a team id
   * @param name team name to be searched for
   * @param cacheOverride override/go past database or not
   * @return response result and body content
   */
  public abstract SimpleEntry<CallResult, String> callMatch(String name, boolean cacheOverride);

  /**
   * Checks if response was successful or failed
   * @param response ApiResponse representing the response data from an api call
   * @return success or error depending on response and either content or error message
   */
  public SimpleEntry<CallResult, String> validateResponseInputApi(ApiResponse response) {
    SimpleEntry<CallResult, String> result;
    int statusCode = response.getStatusCode();
    String body = response.getBody();
    if (statusCode == 200) {
      result = new SimpleEntry<>(CallResult.SUCCESS, body);
    } else {
      ErrorResponseInput ero = new Gson().fromJson(body, ErrorResponseInput.class);
      String msg = ero.getError();
      result = new SimpleEntry<>(CallResult.ERROR, msg);
    }

    return result;
  }

  public void setInputApiCalls(InputApiCalls inputApiCalls) {
    this.inputApiCalls = inputApiCalls;
  }
}
