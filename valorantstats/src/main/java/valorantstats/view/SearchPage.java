package valorantstats.view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;
import valorantstats.presenter.SearchPagePresenter;

import java.util.AbstractMap;

/** Search display and results */
public interface SearchPage {

  /** Clears the text from the search field */
  void clearSearchTextField();

  void setBtnSearch(boolean disable);

  /**
   * Disable button to clear cache
   *
   * @param disable
   */
  void setBtnClearCacheDisable(boolean disable);

  void searchTeam(String name);

  /**
   * Alert user of cache hit
   *
   * @param name searched team
   * @param teamResult cache result
   */
  void processSearchTeamCacheHit(
      String name, AbstractMap.SimpleEntry<CallResult, String> teamResult);

  /**
   * Alert user of cache hit
   *
   * @param name searched team id
   * @param teamResult cache result
   */
  void processSearchMatchCacheHit(
      String name, AbstractMap.SimpleEntry<CallResult, String> teamResult);

  /**
   * Alert user of errored search
   *
   * @param searchTeamResult search result
   */
  void displaySearchTeamError(AbstractMap.SimpleEntry<CallResult, String> searchTeamResult);

  /**
   * Display search results
   *
   * @param team retrieved team from search
   * @param highlightColor user's preferred color
   */
  void displaySearchTeam(Team team, String highlightColor);

  default void highlight(Label lbl, String color) {
    lbl.setStyle(color);
  }

  default void endHighlight(Label lbl) {
    lbl.setStyle("-fx-background-color: TRANSPARENT");
  }

  /**
   * Handle search for team's matches
   *
   * @param name team id
   */
  void handleTeamMatchesClick(String name);

  /**
   * Alert user of errored search
   *
   * @param searchMatchResult search result
   */
  void displayHandleTeamMatchesClickError(
      AbstractMap.SimpleEntry<CallResult, String> searchMatchResult);

  /**
   * Display search results
   *
   * @param matches retrieved matches from search
   * @param highlightColor user's preferred color
   */
  void displayHandleTeamMatchesClick(Match[] matches, String highlightColor);

  void clearPane();

  Alert alertCache(ButtonType cache, ButtonType fresh);

  GridPane getSearchPane();

  GridPane getResultsPane();

  void searchAlert(String errorMsg);

  void showCacheAlert();

  void setSearchPagePresenter(SearchPagePresenter searchPagePresenter);
}
