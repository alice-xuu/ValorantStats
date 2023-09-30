package valorantstats.model.apicalls;

import valorantstats.model.apicalls.dummy.InputDummyApi;

/** Sends requests to a dummy api when the inputApi is specified as offline */
public class inputApiCallsOffline implements InputApiCalls {

  private InputDummyApi inputDummyApi;

  public inputApiCallsOffline() {
    this.inputDummyApi = new InputDummyApi();
  }

  @Override
  public ApiResponse getRequestTeam(String name) {
    return new ApiResponse(200, inputDummyApi.getRequestTeamDummy());
  }

  @Override
  public ApiResponse getRequestTeamUpcomingMatches(String name) {
    return new ApiResponse(200, inputDummyApi.getRequestTeamUpcomingMatchesDummy());
  }

  @Override
  public void setInputDummyApi(InputDummyApi inputDummyApi) {
    this.inputDummyApi = inputDummyApi;
  }
}
