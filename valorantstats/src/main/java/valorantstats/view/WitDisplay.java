package valorantstats.view;

import javafx.scene.layout.GridPane;
import valorantstats.presenter.DisplayPresenterImpl;

/** Page to use Wit functions */
public interface WitDisplay {
  void errorAlert(String message);

  /**
   * Display input bar and button for wit actions
   *
   * @return display
   */
  GridPane getWitDisplay();

  void setDisplayPresenter(DisplayPresenterImpl displayPresenter);
}
