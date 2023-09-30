package valorantstats.view;

import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import valorantstats.model.HistoryItem;
import valorantstats.presenter.DisplayPresenterImpl;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Display {

  /** Sets the scene of the app */
  void displayScene();

  /**
   * Sets the presenter
   *
   * @param displayPresenter presenter that manages user interactions
   */
  void setDisplayPresenter(DisplayPresenterImpl displayPresenter);

  /** Display splash page */
  void showSplashPage();
  /** Display login page */
  void showLoginPage();

  /** Creates menu */
  void buildMenu();

  /**
   * Builds the top menu with the menubar and fast mode toggle
   *
   * @param menuBar menuBar for user navigation
   */
  void buildTopBarMenu(MenuBar menuBar);

  /**
   * Sets the current active searchPage
   *
   * @param searchPage search display for team data
   */
  void setSearchPage(SearchPage searchPage);

  /**
   * Update the fast mode switch state and text
   *
   * @param text string describing fast mode state that the application is in
   * @param mode boolean true if fast mode is on, false if fast mode is off
   * @param isSwitch true if the user interaction was via the switch, switch state updates
   *     automatically, false if user interaction was via menuItems, switch state updates manually
   */
  void updateFastModeSwitch(String text, boolean mode, boolean isSwitch);

  /** Creates bread crumb display */
  void buildBreadCrumbBar();

  /** Creates home page */
  void buildHomePane();

  /** Creates upload image display */
  void buildUploadImagePane();

  /**
   * Disable upload image button
   *
   * @param disable true to disable use
   */
  void setDisableUploadImageButton(boolean disable);

  /**
   * Display upload image info when api call errored
   *
   * @param report string representation of team and match history
   * @param bi bufferedImage of QR code
   * @param errorMsg error message
   */
  void displayUploadImageError(String report, BufferedImage bi, String errorMsg);

  /**
   * Display upload image info when api call success
   *
   * @param report string representation of team and match history
   * @param bi bufferedImage of QR code
   * @param link link to uploaded QR code
   */
  void displayUploadImageSuccess(String report, BufferedImage bi, String link);

  void buildAboutPane(String appName, String name, String references);

  /** Do button click action */
  void activateBtnUploadImage();

  /**
   * Update display with new history list and colorString
   *
   * @param historyList search history
   * @param colorString personalisation colors
   */
  void update(List<HistoryItem> historyList, String colorString);

  void homeAction();

  void aboutAction();

  void searchTeamAction();

  void personalisationAction();

  Scene getScene();

  int getWidth();

  BorderPane getMainPane();
}
