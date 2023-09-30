package valorantstats.model;

import valorantstats.model.enums.CallResult;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Set;

/** Manages user accounts and personalisation */
public interface Personalisation {

  /**
   * Checks if a username already exists in the database, if not, hashes the password and
   *
   * @param username
   * @param password
   * @return
   */
  CallResult createNewAccount(String username, String password);

  /**
   * Checks if the inputted username and password (after hash) is in the database
   *
   * @param username
   * @param password
   * @return
   */
  CallResult verifyExistingAccount(String username, String password);

  /**
   * Gets the currently logged-in user
   *
   * @return username
   */
  String getCurrentUser();

  void setCurrentUser(String currentUser);

  /**
   * Updates user's chosen colors for button, text, highlight, and background in the database
   *
   * @param colors
   */
  void updateBackgroundColor(String colors);

  void updateTextColor(String colors);

  void updateButtonColor(String colors);

  /**
   * Retrieve the current user's color preferences for buttons, background, text from the database
   *
   * @return string of colors
   */
  String getComponentColor(String component);

  String getAllComponentColors();

  /**
   * Retrieve the current user's color preferences for highlighting from the database
   *
   * @return highlight color preference
   */
  String getHighlightColor();

  /**
   * Saves user's current breadcrumb history state in the database
   *
   * @param breadcrumb history in string form
   * @param breadcrumbIndex current navigated place in the history
   */
  void storeBreadcrumb(String breadcrumb, int breadcrumbIndex);

  /**
   * Retrieves current user's breadcrumbIndex (navigated place in the history from the last login)
   * from the database
   *
   * @return int index
   */
  Integer retrieveBreadcrumbIndex();

  /**
   * Retrieves current user's breadcrumb history from the database and converts it into a list
   *
   * @return list representing user's history
   */
  List<HistoryItem> retrieveBreadcrumb();

  /**
   * Updates user's state in the database with their last searched team or match
   *
   * @param currentTeamOrMatch results from search
   */
  void updateCurrentTeamOrMatch(SimpleEntry<CallResult, String> currentTeamOrMatch);

  /**
   * Updates user's state with their current history state and search results
   *
   * @param breadcrumb
   * @param breadcrumbIndex
   * @param currentTeamOrMatch
   */
  void updateUserStateDB(
      String breadcrumb, int breadcrumbIndex, SimpleEntry<CallResult, String> currentTeamOrMatch);

  /**
   * Retrieves the user's last search result from the database
   *
   * @return
   */
  SimpleEntry<CallResult, String> getCurrentTeamOrMatch();

  void addObserverPers(ModelObserver observer);

  Set<ModelObserver> getObserversPers();

  void updateObserversPers();

  /**
   * Updates the current user's color preference for highlighting text
   *
   * @param colorString
   */
  void updateHighlightColor(String colorString);
}
