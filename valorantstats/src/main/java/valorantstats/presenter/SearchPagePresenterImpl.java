package valorantstats.presenter;

import javafx.concurrent.Task;
import valorantstats.model.HistoryItem;
import valorantstats.model.ModelFacade;
import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;
import valorantstats.view.SearchPage;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Implementation of SearchPagePresenter to handle user searches */
public class SearchPagePresenterImpl implements SearchPagePresenter {

  private static final int NUM_THREADS = 4;
  private final ModelFacade model;
  private final ExecutorService pool =
      Executors.newFixedThreadPool(
          NUM_THREADS,
          runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
          });
  private final SearchPage searchPageSlow;
  private final SearchPage searchPageFast;
  private SearchPage searchPage;

  public SearchPagePresenterImpl(
      ModelFacade model, SearchPage searchPage, SearchPage searchPageFast) {
    this.model = model;
    this.searchPage = searchPage;
    this.searchPageSlow = searchPage;
    this.searchPageFast = searchPageFast;
    searchPage.setSearchPagePresenter(this);
    searchPageFast.setSearchPagePresenter(this);

    model.addObserverBackend(this);
  }

  @Override
  public void update() {
    searchPage.setBtnSearch(false);
    boolean fastMode = model.getFastMode();

    if (fastMode) {
      this.searchPage = searchPageFast;
    } else {
      this.searchPage = searchPageSlow;
    }
    List<HistoryItem> historyItemList = model.getHistory();
    if (historyItemList.size() != 0) {
      int currentHistoryIndex = model.getCurrentHistoryIndex();
      HistoryItem historyItem = historyItemList.get(currentHistoryIndex);
      SimpleEntry<CallResult, String> result = historyItem.getSearchResult();
      if (result.getKey() == CallResult.ERROR) {
        searchPage.clearPane();
        return;
      }
      if (historyItem.getSearchType() == SearchType.TEAM) {
        Team team = model.stringToTeamOne(result.getValue());
        searchPage.displaySearchTeam(team, model.getHighlightColor());
      } else if (historyItem.getSearchType() == SearchType.TEAMMATCH) {
        Match[] match = model.stringToMatchArray(result.getValue());
        searchPage.displayHandleTeamMatchesClick(match, model.getHighlightColor());
      }
    }
  }

  @Override
  public void searchTeamInput(String input, boolean cacheOverride) {
    searchPage.clearSearchTextField();
    if (input.equals("")) {
      searchPage.searchAlert("Input can't be empty");
      return;
    }
    runSearchTeamTask(input, cacheOverride);
  }

  @Override
  public void runSearchTeamTask(String name, boolean cacheOverride) {
    searchPage.setBtnSearch(true);
    Task<AbstractMap.SimpleEntry<CallResult, String>> task =
        new Task<AbstractMap.SimpleEntry<CallResult, String>>() {
          @Override
          public AbstractMap.SimpleEntry<CallResult, String> call() throws Exception {
            AbstractMap.SimpleEntry<CallResult, String> result =
                model.searchTeam(name, cacheOverride);
            return result;
          }
        };

    pool.execute(task);
    task.setOnSucceeded(
        (event) -> {
          searchPage.setBtnSearch(false);

          AbstractMap.SimpleEntry<CallResult, String> result = task.getValue();
          if (result.getKey() == CallResult.CACHEHIT) {
            result = model.teamVerifyCachedResult(result);
            searchPage.processSearchTeamCacheHit(name, result);
            return;
          }
          updateTeamModelStates(name, result);

          if (result.getKey() == CallResult.ERROR) {
            model.setCurrentTeamId(null);
            searchPage.displaySearchTeamError(result);
            return;
          }
          Team team = model.stringToTeamOne(result.getValue());
          searchPage.displaySearchTeam(team, model.getHighlightColor());
          model.setCurrentTeamId(team.getId());
        });
  }

  @Override
  public void updateTeamModelStates(String name, SimpleEntry<CallResult, String> result) {
    model.updateCurrentTeamOrMatch(result);
    model.addHistory(SearchType.TEAM, name, result);
    model.updateUserStateDB(model.breadCrumbString(), model.getCurrentHistoryIndex(), result);
  }

  @Override
  public void updateMatchModelStates(String name, SimpleEntry<CallResult, String> result) {
    model.updateCurrentTeamOrMatch(result);
    model.addHistory(SearchType.TEAMMATCH, name, result);
    model.updateUserStateDB(model.breadCrumbString(), model.getCurrentHistoryIndex(), result);
  }

  @Override
  public void clearCache() {
    model.clearCache();
    searchPage.showCacheAlert();
  }

  @Override
  public void teamCacheOptionAction(String name, SimpleEntry<CallResult, String> result) {

    updateTeamModelStates(name, result);
    if (result.getKey() == CallResult.ERROR) {
      model.setCurrentTeamId(null);
      searchPage.displaySearchTeamError(result);
      return;
    }
    Team team = model.stringToTeamOne(result.getValue());
    searchPage.displaySearchTeam(team, model.getHighlightColor());
    model.setCurrentTeamId(team.getId());
  }

  // Match actions

  @Override
  public void matchCacheOptionAction(String name, SimpleEntry<CallResult, String> result) {
    updateMatchModelStates(name, result);
    if (result.getKey() == CallResult.ERROR) {
      searchPage.displayHandleTeamMatchesClickError(result);
    }
    Match[] matches = model.stringToMatchArray(result.getValue());
    searchPage.displayHandleTeamMatchesClick(matches, model.getHighlightColor());
  }

  @Override
  public void runSearchMatchTask(String name, boolean cacheOverride) {
    Task<AbstractMap.SimpleEntry<CallResult, String>> task =
        new Task<SimpleEntry<CallResult, String>>() {
          @Override
          public SimpleEntry<CallResult, String> call() throws Exception {
            SimpleEntry<CallResult, String> result =
                model.searchTeamUpcomingMatches(name, cacheOverride);
            return result;
          }
        };
    pool.execute(task);
    task.setOnSucceeded(
        (event -> {
          SimpleEntry<CallResult, String> matchResult = task.getValue();

          if (matchResult.getKey() == CallResult.CACHEHIT) {
            matchResult = model.matchVerifyCachedResult(matchResult);
            searchPage.processSearchMatchCacheHit(name, matchResult);
            return;
          }
          updateMatchModelStates(name, matchResult);

          if (matchResult.getKey() == CallResult.ERROR) {
            searchPage.displayHandleTeamMatchesClickError(matchResult);
            return;
          }
          Match[] match = model.stringToMatchArray(matchResult.getValue());
          searchPage.displayHandleTeamMatchesClick(match, model.getHighlightColor());
        }));
  }
}
