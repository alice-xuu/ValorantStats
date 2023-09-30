package valorantstats.presenter;

import valorantstats.model.ModelObserver;
import valorantstats.model.enums.CallResult;

import java.util.AbstractMap;

/** Handles user search for teams and matches and modifies view accordingly */
public interface SearchPagePresenter extends ModelObserver {

  /** Updates view when model has changed */
  @Override
  void update();

  /**
   * Handle user input to search for a team
   *
   * @param input user input team name
   * @param cacheOverride cache bypass
   */
  void searchTeamInput(String input, boolean cacheOverride);

  /**
   * Update history and user states for team
   *
   * @param name searched team name
   * @param result outcome of search
   */
  void updateTeamModelStates(String name, AbstractMap.SimpleEntry<CallResult, String> result);

  /**
   * Update history and user states for matches
   *
   * @param name searched team id
   * @param result outcome of search
   */
  void updateMatchModelStates(String name, AbstractMap.SimpleEntry<CallResult, String> result);

  /** Clear stored cache results */
  void clearCache();

  /**
   * Handle searching for a team
   *
   * @param name team name
   * @param cacheOverride cache bypass
   */
  void runSearchTeamTask(String name, boolean cacheOverride);

  /**
   * Handle cache hit and display alert for team search
   *
   * @param name
   * @param result
   */
  void teamCacheOptionAction(String name, AbstractMap.SimpleEntry<CallResult, String> result);

  /**
   * Handle cache hit and display alert for match search
   *
   * @param name
   * @param result
   */
  void matchCacheOptionAction(String name, AbstractMap.SimpleEntry<CallResult, String> result);

  /**
   * Handle searching for a team's matches
   *
   * @param name team name
   * @param cacheOverride cache bypass
   */
  void runSearchMatchTask(String name, boolean cacheOverride);
}
