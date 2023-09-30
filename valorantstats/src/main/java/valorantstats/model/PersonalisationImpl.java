package valorantstats.model;

import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

public class PersonalisationImpl implements Personalisation {

  private final Database db;
  private final Set<ModelObserver> observers;
  private String currentUser;

  public PersonalisationImpl(Database db) {
    this.db = db;
    observers = new HashSet<>();
  }

  @Override
  public CallResult createNewAccount(String username, String password) {

    if (username.equals("") || password.equals("")) {
      return CallResult.ERROR;
    }

    boolean usernameExists = db.queryUsername(username);
    if (usernameExists) {
      return CallResult.ERROR;
    }
    String hashedPassword = sha1Hex(password);

    db.addAccount(username, hashedPassword);

    return CallResult.SUCCESS;
  }

  @Override
  public CallResult verifyExistingAccount(String username, String password) {

    String hashedPassword = sha1Hex(password);
    boolean accountExists = db.queryAccount(username, hashedPassword);

    if (accountExists) {
      return CallResult.SUCCESS;
    }
    return CallResult.ERROR;
  }

  @Override
  public String getCurrentUser() {
    return currentUser;
  }

  @Override
  public void setCurrentUser(String currentUser) {
    this.currentUser = currentUser;
    updateObserversPers();
  }

  @Override
  public void updateBackgroundColor(String colors) {
    db.updateBackgroundColor(this.currentUser, colors);
    updateObserversPers();
  }

  @Override
  public void updateTextColor(String colors) {
    db.updateTextColor(this.currentUser, colors);
    updateObserversPers();
  }

  @Override
  public void updateButtonColor(String colors) {
    db.updateButtonColor(this.currentUser, colors);
    updateObserversPers();
  }

  @Override
  public String getComponentColor(String component) {
    return db.queryComponentColor(this.currentUser, component);
  }

  @Override
  public String getAllComponentColors() {
    String background = db.queryComponentColor(this.currentUser, "backgroundColor");
    String text = db.queryComponentColor(this.currentUser, "textColor");
    String button = db.queryComponentColor(this.currentUser, "buttonColor");

    StringBuilder allComponents = new StringBuilder();
    if (background != null) {
      allComponents.append(background);
    }
    if (text != null) {
      allComponents.append(text);
    }
    if (button != null) {
      allComponents.append(button);
    }
    return allComponents.toString();
  }

  @Override
  public String getHighlightColor() {
    return db.queryHighlightColors(this.currentUser);
  }

  @Override
  public void storeBreadcrumb(String breadcrumb, int breadcrumbIndex) {
    db.updateBreadcrumb(this.currentUser, breadcrumb, breadcrumbIndex);
  }

  @Override
  public Integer retrieveBreadcrumbIndex() {
    return db.queryBreadcrumbIndex(this.currentUser);
  }

  @Override
  public List<HistoryItem> retrieveBreadcrumb() {
    String breadcrumb = db.queryBreadcrumb(this.currentUser);
    if (breadcrumb == null || breadcrumb.equals("")) {
      return new ArrayList<>();
    }
    String[] keyValueHistoryItemStrings = breadcrumb.split("---");
    List<HistoryItem> history = new ArrayList<>();

    for (String historyItem : keyValueHistoryItemStrings) {
      String[] entry = historyItem.split("--");
      SearchType call = null;
      CallResult callResult = null;

      if (entry[0].equals("TEAM")) {
        call = SearchType.TEAM;
      }

      if (entry[0].equals("TEAMMATCH")) {
        call = SearchType.TEAMMATCH;
      }

      if (entry[2].equals("ERROR")) {
        callResult = CallResult.ERROR;
      }
      if (entry[2].equals("SUCCESS")) {
        callResult = CallResult.SUCCESS;
      }

      SimpleEntry<CallResult, String> searchResult = new SimpleEntry<>(callResult, entry[3]);
      history.add(new HistoryItem(call, entry[1], searchResult));
    }

    return history;
  }

  @Override
  public void updateCurrentTeamOrMatch(SimpleEntry<CallResult, String> currentTeamOrMatch) {
    db.updateCurrentTeamOrMatch(
        this.currentUser,
        String.valueOf(currentTeamOrMatch.getKey()),
        currentTeamOrMatch.getValue());
  }

  @Override
  public void updateUserStateDB(
      String breadcrumb, int breadcrumbIndex, SimpleEntry<CallResult, String> currentTeamOrMatch) {
    db.updateBreadcrumb(this.currentUser, breadcrumb, breadcrumbIndex);
    db.updateCurrentTeamOrMatch(
        this.currentUser,
        String.valueOf(currentTeamOrMatch.getKey()),
        currentTeamOrMatch.getValue());
  }

  @Override
  public SimpleEntry<CallResult, String> getCurrentTeamOrMatch() {
    SimpleEntry<String, String> result = db.queryCall(this.currentUser);
    if (result.getKey().equals("ERROR")) {
      return new SimpleEntry<>(CallResult.ERROR, result.getValue());
    }
    if (result.getKey().equals("SUCCESS")) {
      return new SimpleEntry<>(CallResult.SUCCESS, result.getValue());
    }
    return null;
  }

  @Override
  public void addObserverPers(ModelObserver observer) {
    this.observers.add(observer);
  }

  @Override
  public Set<ModelObserver> getObserversPers() {
    return observers;
  }

  @Override
  public void updateObserversPers() {
    for (ModelObserver observer : observers) {
      observer.update();
    }
  }

  @Override
  public void updateHighlightColor(String colorString) {
    db.updateHighlightColors(this.currentUser, colorString);
    updateObserversPers();
  }
}
