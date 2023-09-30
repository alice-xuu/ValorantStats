package valorantstats.model;

import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Set;

public interface DetailsManager {
  /**
   * Gets the history list that represents the pages the user has visited
   *
   * @return history list of history items
   */
  List<HistoryItem> getHistory();

  void setHistory(List<HistoryItem> history);

  /**
   * Adds a history item representing the page the user has visited
   *
   * @param searchType search type identifier
   * @param searchedItem team name or id
   */
  void addHistory(
      SearchType searchType, String searchedItem, SimpleEntry<CallResult, String> searchResult);

  void addObserverBackend(ModelObserver observer);

  Set<ModelObserver> getObserversBackend();

  void updateObserversBackend();

  /**
   * Converts breadcrumb history to short form report string
   *
   * @return string in the form "CALL description, CALL description"
   */
  String shortFormReportString();

  String breadCrumbString();

  String getAppName();

  /**
   * Gets the name of the programmer
   *
   * @return name
   */
  String getName();

  /**
   * Gets the references used for this application
   *
   * @return string
   */
  String getReferences();

  /**
   * Gets the previous history item that the user has accessed
   *
   * @return history item consisting of page identifier and description of page
   */
  HistoryItem getBackHistory();

  /**
   * Current history place of the breadcrumb history the user has navigated to
   *
   * @return index of history
   */
  int getCurrentHistoryIndex();

  void setCurrentHistoryIndex(int historyIndex);

  /**
   * Gets the mode setting of the application
   *
   * @return boolean, true if fast mode is on, false if fast mode is off
   */
  boolean getFastMode();

  /**
   * Sets the mode setting of the application
   *
   * @param selected boolean true if in fast mode, false if not in fast mode
   */
  void setFastMode(boolean selected);
}
