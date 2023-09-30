package valorantstats.model;

import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;

import java.util.*;

/** Implementation of detail manager to manage the application history and details */
public class DetailsManagerImpl implements DetailsManager {
  private static final Object lockObject = new Object();
  private final String appName;
  private final String name;
  private final String references;
  private final Set<ModelObserver> observers;
  private List<HistoryItem> history;
  private int historyIndex = 0;
  private boolean fastMode;

  public DetailsManagerImpl() {
    this.history = new ArrayList<>();
    observers = new HashSet<>();

    this.appName = "Pandascore - Valorant";
    this.name = "Alice";
    this.references =
        "splash image: https://playvalorant.com/en-us/media/\narrow: https://all-free-download.com/free-icon/right-arrow-jpg.html";

    fastMode = false;
  }

  @Override
  public List<HistoryItem> getHistory() {
    return history;
  }

  @Override
  public void setHistory(List<HistoryItem> history) {
    this.history = history;
    updateObserversBackend();
  }

  @Override
  public void addHistory(
      SearchType call,
      String searchedItem,
      AbstractMap.SimpleEntry<CallResult, String> searchResult) {
    HistoryItem historyItem = new HistoryItem(call, searchedItem, searchResult);
    synchronized (lockObject) {
      this.history.add(historyItem);
      setCurrentHistoryIndex(getHistory().size() - 1);
    }
    updateObserversBackend();
  }

  @Override
  public void addObserverBackend(ModelObserver observer) {
    this.observers.add(observer);
  }

  @Override
  public Set<ModelObserver> getObserversBackend() {
    return observers;
  }

  @Override
  public void updateObserversBackend() {
    for (ModelObserver observer : observers) {
      observer.update();
    }
  }

  @Override
  public String shortFormReportString() {
    StringBuilder report = new StringBuilder();
    List<HistoryItem> historyList = getHistory();
    for (int i = 0; i < historyList.size(); i++) {
      HistoryItem historyItem = historyList.get(i);
      if (i != 0) {
        report.append(", ");
      }
      report.append(historyItem.getSearchType()).append(":").append(historyItem.getSearchedItem());
    }
    return String.valueOf(report);
  }

  @Override
  public String breadCrumbString() {
    StringBuilder report = new StringBuilder();
    List<HistoryItem> historyList = getHistory();
    for (int i = 0; i < historyList.size(); i++) {
      HistoryItem historyItem = historyList.get(i);
      if (i != 0) {
        report.append("---");
      }
      report
          .append(historyItem.getSearchType())
          .append("--")
          .append(historyItem.getSearchedItem())
          .append("--")
          .append(historyItem.getSearchResult().getKey())
          .append("--")
          .append(historyItem.getSearchResult().getValue());
    }
    return String.valueOf(report);
  }

  @Override
  public String getAppName() {
    return appName;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getReferences() {
    return references;
  }

  @Override
  public HistoryItem getBackHistory() {
    if (getHistory().size() == 0) {
      return null;
    }
    int index = getCurrentHistoryIndex();
    if (index - 1 < 0) {
      return getHistory().get(0);
    } else {
      index -= 1;
      setCurrentHistoryIndex(index);
      return getHistory().get(index);
    }
  }

  @Override
  public int getCurrentHistoryIndex() {
    return historyIndex;
  }

  @Override
  public void setCurrentHistoryIndex(int historyIndex) {
    this.historyIndex = historyIndex;
  }

  @Override
  public boolean getFastMode() {
    return fastMode;
  }

  @Override
  public void setFastMode(boolean selected) {
    fastMode = selected;
    updateObserversBackend();
  }
}
