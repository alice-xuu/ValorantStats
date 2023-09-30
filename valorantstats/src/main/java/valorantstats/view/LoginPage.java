package valorantstats.view;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import valorantstats.presenter.DisplayPresenterImpl;

/** Login display */
public interface LoginPage {

  /** display for log in to an existing account */
  void existingAccount();

  /** display for creating a new account */
  void newAccount();

  Button createBackButton();

  /** Display to choose between existing or new account */
  void loginChoice();

  Pane getMainLoginPane();

  /**
   * Display error alert
   *
   * @param errorMsg reason for error
   */
  void accountAlert(String errorMsg);

  void setDisplayPresenter(DisplayPresenterImpl displayPresenter);
}
