package valorantstats.model.apicalls;

import valorantstats.model.apicalls.dummy.InputDummyApi;

/** Sends requests to the input api and returns an ApiResponse object */
public interface InputApiCalls {

  /**
   * Send a get request to retrieve data for a team
   *
   * @param name team name for a possible Valorant team
   * @return ApiResponse containing the response status code and body content
   */
  ApiResponse getRequestTeam(String name);

  /**
   * Send a get request to retrieve data for a team's upcoming matches
   *
   * @param name team id belonging to a Valorant team
   * @return ApiResponse containing the response status code and body content
   */
  ApiResponse getRequestTeamUpcomingMatches(String name);

  void setInputDummyApi(InputDummyApi inputDummyApi);
}
