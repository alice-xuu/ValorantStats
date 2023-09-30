package valorantstats.model.apicalls;

import valorantstats.model.apicalls.dummy.OutputDummyApi;

/** Sends requests to the output api */
public interface OutputApiCalls {

  /**
   * Send a request to upload an image representing the short form report (text representation of
   * the user's team/match search history)
   *
   * @param img base64 string of the image to be uploaded
   * @return ApiResponse containing the response status code and body content
   */
  ApiResponse postRequestImgurShortForm(String img);

  void setOutputDummyApi(OutputDummyApi outputDummyApi);
}
