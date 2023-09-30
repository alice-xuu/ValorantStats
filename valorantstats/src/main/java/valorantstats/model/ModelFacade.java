package valorantstats.model;

import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.apicalls.OutputApiCalls;
import valorantstats.model.apicalls.WitAiApiCalls;
import valorantstats.model.callhandlers.Caller;
import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;

import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

/** Provides a facade for the client to use */
public class ModelFacade {

  private final CallManager callManager;
  private final Personalisation personalisation;
  private final CacheManager cacheManager;
  private final WitAiManager witAiManager;
  private final DetailsManager detailsManager;

  public ModelFacade(boolean inputIsOnline, boolean outputIsOnline, Database db) {
    this.personalisation = new PersonalisationImpl(db);
    this.cacheManager = new CacheManagerImpl(db);
    this.callManager = new CallManagerImpl(inputIsOnline, outputIsOnline, db, cacheManager);
    this.witAiManager = new WitAiManagerImpl();
    this.detailsManager = new DetailsManagerImpl();
  }

  // callManager functions

  /**
   * Retrieves information for a team
   *
   * @param name name of a team
   * @param cacheOverride cache bypass
   * @return information of a team if successful, or error message if error
   */
  public SimpleEntry<CallResult, String> searchTeam(String name, boolean cacheOverride) {
    return callManager.searchTeam(name, cacheOverride);
  }

  /**
   * Convert JSON string to a team array
   *
   * @param string json string
   * @return team array
   */
  public Team[] stringToTeamArray(String string) {
    return callManager.stringToTeamArray(string);
  }

  /**
   * Convert JSON string to a single team
   *
   * @param string json string
   * @return team object
   */
  public Team stringToTeamOne(String string) {
    return callManager.stringToTeamOne(string);
  }

  /**
   * Retrieves match data for a team
   *
   * @param name id of a team
   * @param cacheOverride bypass cache
   * @return match information if success
   */
  public SimpleEntry<CallResult, String> searchTeamUpcomingMatches(
      String name, boolean cacheOverride) {
    return callManager.searchTeamUpcomingMatches(name, cacheOverride);
  }

  /**
   * Sanitises user input
   *
   * @param input user input
   * @return validity status of the input
   */
  public boolean validateUserInputName(String input) {
    return callManager.validateInputTeamName(input);
  }

  /**
   * Uploads image to the api
   *
   * @param img string representation of a QR code
   * @return upload status and link if sucesssful
   */
  public SimpleEntry<CallResult, String> uploadImage(String img) {
    return callManager.uploadImage(img);
  }

  /**
   * Converts JSON to a match array
   *
   * @param string JSON string
   * @return match array
   */
  public Match[] stringToMatchArray(String string) {
    return callManager.stringToMatchArray(string);
  }

  /**
   * Creates a bufferedImage from data
   *
   * @param data short form report string (team and match history)
   * @return bufferedImage that represents the data
   */
  public BufferedImage createQR(String data) {
    return callManager.createQR(data);
  }

  /**
   * Converts bufferedImage to a base 64 string
   *
   * @param bi bufferedImage representing short form report
   * @return base64 string representation of bi
   */
  public String bufferedImageToB64(BufferedImage bi) {
    return callManager.bufferedImageToB64(bi);
  }

  /**
   * Checks if input api is online
   *
   * @return input api status
   */
  public boolean isInputIsOnline() {
    return callManager.isInputIsOnline();
  }

  public InputApiCalls getInputApiCalls() {
    return callManager.getInputApiCalls();
  }

  public void setInputApiCalls(InputApiCalls inputApiCalls) {
    callManager.setInputApiCalls(inputApiCalls);
  }

  public OutputApiCalls getOutputApiCalls() {
    return callManager.getOutputApiCalls();
  }

  public void setOutputApiCalls(OutputApiCalls outputApiCalls) {
    callManager.setOutputApiCalls(outputApiCalls);
  }

  /**
   * Retrieves the last i.e. current team accessed or retrieved by user
   *
   * @return team id
   */
  public Integer getCurrentTeamId() {
    return callManager.getCurrentTeamId();
  }

  /**
   * Sets the last i.e. current team id accessed or retrieved by user
   *
   * @param currentTeamId id of current accessed team
   */
  public void setCurrentTeamId(Integer currentTeamId) {
    callManager.setCurrentTeamId(currentTeamId);
  }

  public Caller getApiCaller() {
    return callManager.getApiCaller();
  }

  // cacheManager

  /** clear the cache of all entries */
  public void clearCache() {
    cacheManager.clearCache();
  }

  /**
   * Store a new database entry in the team database
   *
   * @param teamName name of team to be stored
   * @param results results from api call to be stored
   */
  public void cacheTeam(String teamName, String results) {
    cacheManager.cacheTeam(teamName, results);
  }

  /**
   * Store a new database entry in the match database
   *
   * @param teamId id of team to be stored
   * @param results results from api call to be stored
   */
  public void cacheMatch(String teamId, String results) {
    cacheManager.cacheMatch(teamId, results);
  }

  /**
   * Search for a database entry in the team cache
   *
   * @param teamName team name to query
   * @return result of query
   */
  public SimpleEntry<CallResult, String> searchCacheTeam(String teamName) {
    return cacheManager.searchCacheTeam(teamName);
  }

  /**
   * Search for a database entry in the match cache
   *
   * @param teamId team id to query
   * @return result of query
   */
  public SimpleEntry<CallResult, String> searchCacheMatch(String teamId) {
    return cacheManager.searchCacheMatch(teamId);
  }

  /**
   * Updates existing database entry in the match database
   *
   * @param teamName nameof team to be stored
   * @param results results from api call to be stored
   */
  public void updateCacheTeam(String teamName, String results) {
    cacheManager.updateCacheTeam(teamName, results);
  }

  /**
   * Updates existing database entry in the match database
   *
   * @param teamId id of team to be stored
   * @param results results from api call to be stored
   */
  public void updateCacheMatch(String teamId, String results) {
    cacheManager.updateCacheMatch(teamId, results);
  }

  /**
   * Checks if cached result team exists or not and converts the result to error or success
   *
   * @param cacheResult result from a cache check
   * @return converted cacheResult
   */
  public SimpleEntry<CallResult, String> teamVerifyCachedResult(
      SimpleEntry<CallResult, String> cacheResult) {
    return cacheManager.teamVerifyCachedResult(cacheResult);
  }

  /**
   * Checks if cached result match exists or not and converts the result to error or success
   *
   * @param cacheResult result from a cache check
   * @return converted cacheResult
   */
  public SimpleEntry<CallResult, String> matchVerifyCachedResult(
      SimpleEntry<CallResult, String> cacheResult) {
    return cacheManager.matchVerifyCachedResult(cacheResult);
  }

  // DetailsManager

  /**
   * Gets history of searched teams and matches
   *
   * @return list of history items
   */
  public List<HistoryItem> getHistory() {
    return detailsManager.getHistory();
  }

  /**
   * Sets the history list
   *
   * @param history
   */
  public void setHistory(List<HistoryItem> history) {
    detailsManager.setHistory(history);
  }

  /**
   * Add a searched item to the history list
   *
   * @param searchType team or match search
   * @param searchedItem team name or id
   * @param searchResult results of the search
   */
  public void addHistory(
      SearchType searchType, String searchedItem, SimpleEntry<CallResult, String> searchResult) {
    detailsManager.addHistory(searchType, searchedItem, searchResult);
  }

  /**
   * Adds observer
   *
   * @param observer observer that will monitor the backend
   */
  public void addObserverBackend(ModelObserver observer) {
    detailsManager.addObserverBackend(observer);
  }

  /**
   * Gets a short form report string which is the team and match search history
   *
   * @return string representation of searched teams and matches
   */
  public String shortFormReportString() {
    return detailsManager.shortFormReportString();
  }

  /**
   * Gets a string representation of the entire history list including search type, item, results
   *
   * @return string representation of history list
   */
  public String breadCrumbString() {
    return detailsManager.breadCrumbString();
  }

  /**
   * Get the name of the application
   *
   * @return application name
   */
  public String getAppName() {
    return detailsManager.getAppName();
  }

  /**
   * Get the name of the programmer
   *
   * @return name
   */
  public String getName() {
    return detailsManager.getName();
  }

  /**
   * Get the references used for the application
   *
   * @return string of references
   */
  public String getReferences() {
    return detailsManager.getReferences();
  }

  /**
   * Gets the previous item of the history if there is a history
   *
   * @return historyItem representation of previous search
   */
  public HistoryItem getBackHistory() {
    return detailsManager.getBackHistory();
  }

  /**
   * Get currently accessed item in the history
   *
   * @return index of history list
   */
  public int getCurrentHistoryIndex() {
    return detailsManager.getCurrentHistoryIndex();
  }

  /** Set currently accessed item of the history */
  public void setCurrentHistoryIndex(int historyIndex) {
    detailsManager.setCurrentHistoryIndex(historyIndex);
  }

  // personalisation functions

  /**
   * Adds a new account into the database
   *
   * @param username user's desired name
   * @param password user's desired password
   * @return success status of the add
   */
  public CallResult createNewAccount(String username, String password) {
    return personalisation.createNewAccount(username, password);
  }

  /**
   * Checks if the entered details exist in the database
   *
   * @param username name to be queried
   * @param password password to be queried
   * @return success status of the add
   */
  public CallResult verifyExistingAccount(String username, String password) {
    return personalisation.verifyExistingAccount(username, password);
  }

  public String getCurrentUser() {
    return personalisation.getCurrentUser();
  }

  public void setCurrentUser(String currentUser) {
    personalisation.setCurrentUser(currentUser);
  }

  /**
   * Gets the stored color string of the desired component
   *
   * @param component ui component such as background, text, button
   * @return string representation of color
   */
  public String getComponentColor(String component) {
    return personalisation.getComponentColor(component);
  }

  /**
   * Gets the stored color string of all the components
   *
   * @return string representation of all stored colors
   */
  public String getAllComponentColors() {
    return personalisation.getAllComponentColors();
  }

  /**
   * Updates user's preferred background color
   *
   * @param color string representation of background color style
   */
  public void updateBackgroundColor(String color) {
    personalisation.updateBackgroundColor(color);
  }

  /**
   * Updates user's preferred text color
   *
   * @param color string representation of text color style
   */
  public void updateTextColor(String color) {
    personalisation.updateTextColor(color);
  }

  /**
   * Updates user's preferred button color
   *
   * @param color string representation of button color style
   */
  public void updateButtonColor(String color) {
    personalisation.updateButtonColor(color);
  }

  /**
   * Store the user's history
   *
   * @param breadcrumb text representation of history list
   * @param breadcrumbIndex current i.e. last accessed history item
   */
  public void storeBreadcrumb(String breadcrumb, int breadcrumbIndex) {
    personalisation.storeBreadcrumb(breadcrumb, breadcrumbIndex);
  }

  public Integer getBreadcrumbIndex() {
    return personalisation.retrieveBreadcrumbIndex();
  }

  /**
   * Get the user's history retrieved from the database
   *
   * @return history list
   */
  public List<HistoryItem> retrieveBreadcrumb() {
    return personalisation.retrieveBreadcrumb();
  }

  /**
   * Update user's currently accessed team or match results in the database
   *
   * @param currentTeamOrMatch team or match call results
   */
  public void updateCurrentTeamOrMatch(SimpleEntry<CallResult, String> currentTeamOrMatch) {
    personalisation.updateCurrentTeamOrMatch(currentTeamOrMatch);
  }

  public SimpleEntry<CallResult, String> getCurrentTeamOrMatch() {
    return personalisation.getCurrentTeamOrMatch();
  }

  /**
   * Update user's stored history, history place (index) and current accessed item
   *
   * @param breadcrumb string representation of history list
   * @param breadcrumbIndex history index
   * @param currentTeamOrMatch current accessed item results
   */
  public void updateUserStateDB(
      String breadcrumb, int breadcrumbIndex, SimpleEntry<CallResult, String> currentTeamOrMatch) {
    personalisation.updateUserStateDB(breadcrumb, breadcrumbIndex, currentTeamOrMatch);
  }

  public void addObserverPers(ModelObserver observer) {
    personalisation.addObserverPers(observer);
  }

  /**
   * Updates user's preferred highlight color
   *
   * @param colorString string representation of highlight color style
   */
  public void updateHighlightColor(String colorString) {
    personalisation.updateHighlightColor(colorString);
  }

  public String getHighlightColor() {
    return personalisation.getHighlightColor();
  }

  // WitAiManager

  /**
   * Return the meaning of a sentence from Wit.Ai
   *
   * @param input user input phrase
   * @return WitItem representing user intent and desired actions
   */
  public WitItem retrieveSentenceMeaning(String input) {
    return witAiManager.retrieveSentenceMeaning(input);
  }

  public void setWitAiApiCalls(WitAiApiCalls witAiApiCalls) {
    witAiManager.setWitAiApiCalls(witAiApiCalls);
  }

  /**
   * Get the state of the mode
   * @return boolean, true if fast mode is on, false if fast mode is off
   */
  public boolean getFastMode() {
    return detailsManager.getFastMode();
  }

  /**
   * Set the state of the mode
   * @param selected true if fast mode is on, false if fast mode is off
   */
  public void setFastMode(boolean selected) {
    detailsManager.setFastMode(selected);
  }
}
