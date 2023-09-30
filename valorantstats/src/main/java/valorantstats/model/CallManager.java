package valorantstats.model;

import valorantstats.model.apicalls.ApiResponse;
import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.apicalls.OutputApiCalls;
import valorantstats.model.callhandlers.Caller;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.ImgurData;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;

import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;

/** Manages searches for teams and matches */
public interface CallManager {

  /**
   * Searches for the team
   *
   * @param name name of team
   * @return CallResult.ERROR and an error message if the team doesn't exist, CallResult.SUCCESS and
   *     data from an api call if the team exists
   */
  SimpleEntry<CallResult, String> searchTeam(String name, boolean cacheOverride);

  /**
   * Converts json string to a team array
   *
   * @param string to be converted
   * @return array of teams
   */
  Team[] stringToTeamArray(String string);

  /**
   * Converts json string to get a single team
   *
   * @param string to be converted
   * @return Team object
   */
  Team stringToTeamOne(String string);

  /**
   * Searches for a team's upcoming matches
   *
   * @param teamId id of team
   * @return CallResult.ERROR and an error message if there aren't any matches, CallResult.SUCCESS
   *     and data from an api call if the team exists
   */
  SimpleEntry<CallResult, String> searchTeamUpcomingMatches(String teamId, boolean cacheOverride);

  /**
   * Sanitisation for user input
   *
   * @param input user input text
   * @return false if empty or an integer (teams are searched by name)
   */
  boolean validateInputTeamName(String input);

  /**
   * Makes an api call to upload an image
   *
   * @param img in string format representing an image
   * @return ERROR or SUCCESS based on the api call
   */
  SimpleEntry<CallResult, String> uploadImage(String img);

  /**
   * Converts json string to a match array
   *
   * @param string json format string
   * @return Match array
   */
  Match[] stringToMatchArray(String string);

  ImgurData stringToImgurData(String string);

  /**
   * Converts string data to a BufferedImage and saves the image to a file
   *
   * @param data short form report
   * @return data as a BufferedImage
   */
  BufferedImage createQR(String data);

  /**
   * Gets the file name of the qrcode
   *
   * @return file name
   */
  String getQrCodeSF();

  /**
   * Converts a BufferedImage to a base 64 string
   *
   * @param bi image to be converted
   * @return base 64 string
   */
  String bufferedImageToB64(BufferedImage bi);

  boolean isInputIsOnline();

  boolean isOutputIsOnline();

  InputApiCalls getInputApiCalls();

  void setInputApiCalls(InputApiCalls inputApiCalls);

  OutputApiCalls getOutputApiCalls();

  void setOutputApiCalls(OutputApiCalls outputApiCalls);

  /**
   * Checks if an error occurred when making the request
   *
   * @param response of api call
   * @return
   */
  SimpleEntry<CallResult, String> validateResponseOutputApi(ApiResponse response);

  Integer getCurrentTeamId();

  void setCurrentTeamId(Integer currentTeamId);

  Caller getChain();

  Caller getApiCaller();
}
