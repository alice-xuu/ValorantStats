package valorantstats.presenter;

import javafx.concurrent.Task;
import valorantstats.model.HistoryItem;
import valorantstats.model.ModelObserver;
import valorantstats.model.WitItem;
import valorantstats.model.enums.CallResult;

import java.util.AbstractMap;

/** Handles user interactions and modifies view accordingly */
public interface DisplayPresenter extends ModelObserver {

  /** Updates view when model has changed */
  @Override
  void update();

  /** Handles user going back in history using a back button */
  void goBackAction();

  /**
   * Handles user selecting an item in the breadcrumb
   *
   * @param historyItem
   * @param finalIndex
   */
  void breadcrumbClickAction(HistoryItem historyItem, int finalIndex);

  /**
   * Determines how view changes based on the history item
   *
   * @param historyItem searched item
   */
  void historyActions(HistoryItem historyItem);

  /** Displays the home page */
  void showHomePage();

  /** Displays about page */
  void showAboutPage();

  /** Displays search page */
  void showSearchPage();

  /** Displays personalisation page */
  void showPersonalisationPage();

  /** Displays login page */
  void showLoginPage();

  /** Displays existing account input page */
  void showExistingAccountPage();

  /** Displays new account creation page */
  void showNewAccountPage();

  /** Handles uploading image */
  void uploadImageAction();

  /**
   * Task to handle image upload
   *
   * @param imgData string representation of QR code
   * @return task
   */
  Task<AbstractMap.SimpleEntry<CallResult, String>> createUploadImageTask(String imgData);

  /**
   * Handles submitting user details
   *
   * @param username user input username
   * @param password user input password
   */
  void existingAccountSubmitAction(String username, String password);

  /**
   * Handles account creation
   *
   * @param username user input username
   * @param password user input password
   */
  void newAccountSubmitAction(String username, String password);

  /** Displays login choice page */
  void showLoginChoicePage();

  /**
   * Updates user's background color
   *
   * @param colorString color style
   */
  void updateBackgroundColor(String colorString);

  /**
   * Updates user's text color
   *
   * @param colorString color style
   */
  void updateTextColor(String colorString);

  /**
   * Updates user's button color
   *
   * @param colorString color style
   */
  void updateButtonColor(String colorString);

  /**
   * Updates user's highlight color
   *
   * @param colorString color style
   */
  void updateHighlightColor(String colorString);

  /**
   * Handles wit action button
   *
   * @param input user input phrase
   */
  void witButtonAction(String input);

  /**
   * Handles user's intent
   *
   * @param witItem object representation of Wit's output of input meaning
   */
  void witAction(WitItem witItem);

  /**
   * Wit api call task
   *
   * @param input user input phrase/sentence
   * @return task
   */
  Task<WitItem> createWitMessageTask(String input);

  void setFastMode(boolean selected, boolean isSwitch);
}
