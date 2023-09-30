package valorantstats.model;

import valorantstats.model.apicalls.ApiResponse;
import valorantstats.model.apicalls.WitAiApiCalls;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.WitResponse;

import java.util.AbstractMap;

/** Manages Wit.Ai api calls */
public interface WitAiManager {

  /**
   * Sends request to Wit Api and converts api response to a WitItem representing the input's
   * desired actions
   *
   * @param input sentence or phrase
   * @return WitItem representation of intent
   */
  WitItem retrieveSentenceMeaning(String input);

  /**
   * Convert JSON string to a WitResponse object
   *
   * @param string JSON string
   * @return WitResponse object containing intent data
   */
  WitResponse stringToWitData(String string);

  /**
   * Checks api call outcome
   *
   * @param response response from request
   * @return call outcome with response or error message
   */
  AbstractMap.SimpleEntry<CallResult, String> validateResponseWitApi(ApiResponse response);

  void setWitAiApiCalls(WitAiApiCalls witAiApiCalls);
}
