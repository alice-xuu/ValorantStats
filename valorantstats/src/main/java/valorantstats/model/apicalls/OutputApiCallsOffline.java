package valorantstats.model.apicalls;

import valorantstats.model.apicalls.dummy.OutputDummyApi;

/** Sends requests to a dummy api when the output is specified as offline */
public class OutputApiCallsOffline implements OutputApiCalls {
  private OutputDummyApi outputDummyApi;

  public OutputApiCallsOffline() {
    this.outputDummyApi = new OutputDummyApi();
  }

  @Override
  public ApiResponse postRequestImgurShortForm(String img) {
    return new ApiResponse(200, outputDummyApi.postRequestImgurShortFormDummy());
  }

  @Override
  public void setOutputDummyApi(OutputDummyApi outputDummyApi) {
    this.outputDummyApi = outputDummyApi;
  }
}
