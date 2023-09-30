package valorantstats.view;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import valorantstats.presenter.DisplayPresenterImpl;

/** Implementation of WitDisplay to handle user interaction with Wit */
public class WitDisplayImpl implements WitDisplay {
  private GridPane witDisplay;
  private DisplayPresenterImpl displayPresenter;

  public WitDisplayImpl() {

    witDisplay = new GridPane();
    witDisplay.setVgap(20);
    witDisplay.setHgap(20);
    witDisplay.setPadding(new Insets(20));

    Label lblAction =
        new Label(
            "Use Wit.Ai to interact with this app. You can input something like \"Navigate to about page\" or \"Search for Sentinels\"");
    lblAction.setWrapText(true);
    TextField tfAction = new TextField();
    Button btnAction = new Button();
    btnAction.setText("Action");
    btnAction.setOnAction(
        (event) -> {
          displayPresenter.witButtonAction(tfAction.getText());
        });
    witDisplay.addRow(0, lblAction);
    witDisplay.addRow(1, new HBox(tfAction, btnAction));
  }

  @Override
  public void errorAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Wit.ai alert");
    alert.setHeaderText(message);

    alert.showAndWait();
  }

  @Override
  public GridPane getWitDisplay() {
    return witDisplay;
  }

  @Override
  public void setDisplayPresenter(DisplayPresenterImpl displayPresenter) {
    this.displayPresenter = displayPresenter;
  }
}
