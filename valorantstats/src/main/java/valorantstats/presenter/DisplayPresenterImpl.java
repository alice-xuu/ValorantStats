package valorantstats.presenter;

import javafx.concurrent.Task;
import javafx.scene.paint.Color;
import valorantstats.model.HistoryItem;
import valorantstats.model.ModelFacade;
import valorantstats.model.WitItem;
import valorantstats.model.enums.*;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;
import valorantstats.view.*;

import java.awt.image.BufferedImage;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Implementation of DisplayPresenter to manage user interaction and concurrency */
public class DisplayPresenterImpl implements DisplayPresenter {

  private static final int NUM_THREADS = 4;
  private final ExecutorService pool =
      Executors.newFixedThreadPool(
          NUM_THREADS,
          runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
          });
  private final SplashPage splashPage;
  private final LoginPage loginPage;
  private final PersonalisationPageImpl personalisationPage;
  private final SearchPage searchPageSlow;
  private final SearchPage searchPageFast;
  private final WitDisplay witDisplay;
  ModelFacade model;
  Display display;
  private SearchPage searchPage;

  public DisplayPresenterImpl(
      ModelFacade model,
      Display display,
      SplashPage splashPage,
      LoginPage loginPage,
      PersonalisationPageImpl personalisationPage,
      SearchPage searchPage,
      WitDisplay witDisplay,
      SearchPage searchPageFast) {
    this.model = model;
    this.display = display;
    this.splashPage = splashPage;
    this.loginPage = loginPage;
    this.personalisationPage = personalisationPage;
    this.searchPage = searchPage;
    this.searchPageSlow = searchPage;
    this.searchPageFast = searchPageFast;
    this.witDisplay = witDisplay;

    model.addObserverBackend(this);
    model.addObserverPers(this);

    display.setDisplayPresenter(this);
    splashPage.setDisplayPresenter(this);
    loginPage.setDisplayPresenter(this);
    personalisationPage.setDisplayPresenter(this);
    witDisplay.setDisplayPresenter(this);

    display.displayScene();
    display.showSplashPage();
  }

  @Override
  public void update() {
    List<HistoryItem> historyList = model.getHistory();
    String colorString = model.getAllComponentColors();
    display.update(historyList, colorString);
  }

  @Override
  public void goBackAction() {
    HistoryItem back = model.getBackHistory();
    model.storeBreadcrumb(model.breadCrumbString(), model.getCurrentHistoryIndex());
    historyActions(back);
  }

  @Override
  public void breadcrumbClickAction(HistoryItem historyItem, int finalIndex) {
    model.setCurrentHistoryIndex(finalIndex);
    model.storeBreadcrumb(model.breadCrumbString(), model.getCurrentHistoryIndex());
    historyActions(historyItem);
  }

  @Override
  public void historyActions(HistoryItem historyItem) {

    if (historyItem.getSearchType() == SearchType.TEAM) {
      display.searchTeamAction();
      SimpleEntry<CallResult, String> result = historyItem.getSearchResult();
      if (result.getKey() == CallResult.ERROR) {
        model.setCurrentTeamId(null);
        searchPage.displaySearchTeamError(result);
        return;
      }
      Team team = model.stringToTeamOne(result.getValue());
      searchPage.displaySearchTeam(team, model.getHighlightColor());
      model.setCurrentTeamId(team.getId());
      searchPage.displaySearchTeam(team, model.getHighlightColor());
    } else if (historyItem.getSearchType() == SearchType.TEAMMATCH) {
      display.searchTeamAction();
      SimpleEntry<CallResult, String> matchResult = historyItem.getSearchResult();

      if (matchResult.getKey() == CallResult.ERROR) {
        searchPage.displayHandleTeamMatchesClickError(matchResult);
        return;
      }
      Match[] match = model.stringToMatchArray(matchResult.getValue());
      searchPage.displayHandleTeamMatchesClick(match, model.getHighlightColor());
    }
  }

  @Override
  public void showHomePage() {
    display.homeAction();
    display.buildUploadImagePane();
    if (model.getHistory().size() < 1) {
      display.setDisableUploadImageButton(true);
    }
  }

  @Override
  public void showAboutPage() {
    display.buildAboutPane(model.getAppName(), model.getName(), model.getReferences());
    display.aboutAction();
  }

  @Override
  public void showSearchPage() {
    display.searchTeamAction();
    if (!model.isInputIsOnline()) {
      searchPage.setBtnClearCacheDisable(true);
    }
  }

  @Override
  public void showPersonalisationPage() {
    personalisationPage.buildPreviewPane(model.getHighlightColor());
    display.personalisationAction();
  }

  @Override
  public void showLoginPage() {
    display.showLoginPage();
  }

  @Override
  public void showExistingAccountPage() {
    loginPage.existingAccount();
  }

  @Override
  public void showNewAccountPage() {
    loginPage.newAccount();
  }

  @Override
  public void uploadImageAction() {

    String report = model.shortFormReportString();
    BufferedImage bi = model.createQR(report);

    Task<SimpleEntry<CallResult, String>> taskUpload =
        createUploadImageTask(model.bufferedImageToB64(bi));
    taskUpload.setOnSucceeded(
        event1 -> {
          if (taskUpload.getValue().getKey() == CallResult.ERROR) {
            display.buildUploadImagePane();
            display.displayUploadImageError(report, bi, taskUpload.getValue().getValue());
          } else {
            display.buildUploadImagePane();
            display.displayUploadImageSuccess(report, bi, taskUpload.getValue().getValue());
          }
        });
    pool.execute(taskUpload);
  }

  @Override
  public Task<SimpleEntry<CallResult, String>> createUploadImageTask(String imgData) {
    Task<SimpleEntry<CallResult, String>> task =
        new Task<>() {
          @Override
          public SimpleEntry<CallResult, String> call() throws Exception {
            SimpleEntry<CallResult, String> result = model.uploadImage(imgData);
            return result;
          }
        };
    return task;
  }

  @Override
  public void existingAccountSubmitAction(String username, String password) {
    if (username.equals("") || password.equals("")) {
      loginPage.accountAlert("Username or password can't be empty");
      return;
    }

    CallResult result = model.verifyExistingAccount(username, password);
    if (result.equals(CallResult.SUCCESS)) {
      model.setCurrentUser(username);
      List<HistoryItem> breadcrumb = model.retrieveBreadcrumb();
      model.setHistory(breadcrumb);
      if (breadcrumb.size() > 0) {
        int breadCrumbIndex = model.getBreadcrumbIndex();
        historyActions(breadcrumb.get(breadCrumbIndex));
        model.setCurrentHistoryIndex(breadCrumbIndex);
      } else {
        showHomePage();
      }
    } else {
      loginPage.accountAlert("Account not found");
      return;
    }
  }

  @Override
  public void newAccountSubmitAction(String username, String password) {
    if (username.equals("") || password.equals("")) {
      loginPage.accountAlert("Username or password can't be empty");
      return;
    }
    CallResult result = model.createNewAccount(username, password);

    if (result.equals(CallResult.SUCCESS)) {
      model.setCurrentUser(username);

      personalisationPage.updateBackgroundColorString(Color.web("#364966"));
      personalisationPage.updateTextColorString(Color.WHITE);
      personalisationPage.updateButtonColorString(Color.web("#dc3d4b"));
      personalisationPage.updateHighlightColorString(Color.web("#b78460"));
      showPersonalisationPage();
      model.storeBreadcrumb(model.breadCrumbString(), model.getCurrentHistoryIndex());

    } else {
      loginPage.accountAlert("Username already exists");
    }
  }

  @Override
  public void showLoginChoicePage() {
    loginPage.loginChoice();
  }

  @Override
  public void updateBackgroundColor(String colorString) {
    model.updateBackgroundColor(colorString);
  }

  @Override
  public void updateTextColor(String colorString) {
    model.updateTextColor(colorString);
  }

  @Override
  public void updateButtonColor(String colorString) {
    model.updateButtonColor(colorString);
  }

  @Override
  public void updateHighlightColor(String colorString) {
    model.updateHighlightColor(colorString);
  }

  @Override
  public void witButtonAction(String input) {
    if (input.equals("")) {
      witDisplay.errorAlert("Input can't be empty");
      return;
    }
    Task<WitItem> witMessageTask = createWitMessageTask(input);
    witMessageTask.setOnSucceeded(
        event1 -> {
          witAction(witMessageTask.getValue());
        });
    pool.execute(witMessageTask);
  }

  @Override
  public void witAction(WitItem witItem) {

    if (witItem.getCallResult() == CallResult.ERROR) {
      witDisplay.errorAlert(witItem.getErrorMsg());
    } else if (witItem.getIntent() == MsgIntent.CHANGECOLOUR) {
      ComponentType component = witItem.getComponentEntity();
      String colourString = witItem.getColourEntity();
      Color color;
      try {
        color = Color.web(colourString);
      } catch (IllegalArgumentException e) {
        witDisplay.errorAlert("Not a valid color");
        return;
      }

      if (component.equals(ComponentType.BACKGROUND)) {
        personalisationPage.updateBackgroundColorString(color);
      }
      if (component.equals(ComponentType.TEXT)) {
        personalisationPage.updateTextColorString(color);
      }
      if (component.equals(ComponentType.BUTTON)) {
        personalisationPage.updateButtonColorString(color);
      }
      if (component.equals(ComponentType.HIGHLIGHT)) {
        personalisationPage.updateHighlightColorString(color);
      }
    } else if (witItem.getIntent() == MsgIntent.NAVIGATE) {
      PageType page = witItem.getPageEntity();
      if (page.equals(PageType.SEARCH)) {
        showSearchPage();
      }
      if (page.equals(PageType.PERSONALISE)) {
        showPersonalisationPage();
      }
      if (page.equals(PageType.ABOUT)) {
        showAboutPage();
      }
      if (page.equals(PageType.HOME)) {
        showHomePage();
      }

    } else if (witItem.getIntent() == MsgIntent.SEARCHTEAM) {
      String teamName = witItem.getTeamNameEntity();
      showSearchPage();
      searchPage.searchTeam(teamName);
    } else if (witItem.getIntent() == MsgIntent.SEARCHMATCHES) {
      if (model.getCurrentTeamId() == null) {
        witDisplay.errorAlert("search for a valid team first");
      } else {
        showSearchPage();
        searchPage.handleTeamMatchesClick(model.getCurrentTeamId().toString());
      }
    } else if (witItem.getIntent() == MsgIntent.UPLOADIMAGE) {
      showHomePage();
      display.activateBtnUploadImage();
    } else {
      witDisplay.errorAlert("No valid action found");
    }
  }

  @Override
  public Task<WitItem> createWitMessageTask(String input) {
    return new Task<WitItem>() {
      @Override
      public WitItem call() throws Exception {
        WitItem witItem = model.retrieveSentenceMeaning(input);
        return witItem;
      }
    };
  }

  @Override
  public void setFastMode(boolean selected, boolean isSwitch) {
    model.setFastMode(selected);

    boolean onSearchPage = false;

    // check if the currently displayed page is the search page
    if (display.getMainPane().getCenter() == searchPage.getSearchPane()) {
      onSearchPage = true;
    }

    // if selected, fast mode is on
    if (selected) {
      this.searchPage = searchPageFast;
      display.setSearchPage(searchPageFast);
      display.updateFastModeSwitch("Fast Mode: On", true, isSwitch);
    } else {
      this.searchPage = searchPageSlow;
      display.setSearchPage(searchPageSlow);
      display.updateFastModeSwitch("Fast Mode: Off", false, isSwitch);
    }
    if (onSearchPage) {
      display.searchTeamAction();
    }
  }
}
