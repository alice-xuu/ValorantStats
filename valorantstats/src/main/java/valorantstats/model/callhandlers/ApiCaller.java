package valorantstats.model.callhandlers;

import valorantstats.model.CacheManager;
import valorantstats.model.apicalls.ApiResponse;
import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.enums.CallResult;

import java.util.AbstractMap.SimpleEntry;

/** Calls the real or dummy api methods to search for a team or match */
public class ApiCaller extends Caller {

  public ApiCaller(CacheManager cacheManager, InputApiCalls inputApiCalls) {
    super(cacheManager, inputApiCalls);
  }

  /**
   * Calls a request for a team name, validates the api response, inserts/updates cache database
   *
   * @param name team name to retrieve information for
   * @param cacheOverride true indicates to update database entry, false indicates to insert a new
   *     database entry
   * @return CallResult - either success or error depending on api call, String - either information
   *     for team or error message
   */
  @Override
  public SimpleEntry<CallResult, String> callTeam(String name, boolean cacheOverride) {
    ApiResponse response = inputApiCalls.getRequestTeam(name);
    SimpleEntry<CallResult, String> result = validateResponseInputApi(response);
    if (cacheOverride) {
      cacheManager.updateCacheTeam(name, result.getValue());
    } else {
      cacheManager.cacheTeam(name, result.getValue());
    }
    return result;
  }

  /**
   * * Calls a request for a team id, validates the api response, inserts/updates cache database
   *
   * @param name team id representing a team
   * @param cacheOverride true indicates to update database entry, false indicates to insert a new
   *     database entry
   * @return CallResult - either success or error depending on api call, String - either information
   *     for team or error message
   */
  @Override
  public SimpleEntry<CallResult, String> callMatch(String name, boolean cacheOverride) {
    ApiResponse response = inputApiCalls.getRequestTeamUpcomingMatches(name);
    SimpleEntry<CallResult, String> result = validateResponseInputApi(response);
    if (cacheOverride) {
      cacheManager.updateCacheMatch(name, result.getValue());
    } else {
      cacheManager.cacheMatch(name, result.getValue());
    }
    return result;
  }
}
