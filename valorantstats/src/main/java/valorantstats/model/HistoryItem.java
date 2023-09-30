package valorantstats.model;

import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;

import java.util.AbstractMap.SimpleEntry;

/**
 * Represents a history object containing information about the type of search, what was searched
 * for and the results
 */
public class HistoryItem {
  SearchType searchType;
  String searchedItem;
  SimpleEntry<CallResult, String> searchResult;

  public HistoryItem(
      SearchType searchType, String searchedItem, SimpleEntry<CallResult, String> searchResult) {
    this.searchType = searchType;
    this.searchedItem = searchedItem;
    this.searchResult = searchResult;
  }

  public SearchType getSearchType() {
    return searchType;
  }

  public String getSearchedItem() {
    return searchedItem;
  }

  public SimpleEntry<CallResult, String> getSearchResult() {
    return searchResult;
  }
}
