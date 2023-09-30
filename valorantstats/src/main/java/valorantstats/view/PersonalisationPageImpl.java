package valorantstats.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import valorantstats.presenter.DisplayPresenterImpl;

/** Implementation of personalisationPage for user colors */
public class PersonalisationPageImpl implements PersonalisationPage {

  private final GridPane personalisationPage;
  private final GridPane previewPane;
  private DisplayPresenterImpl displayPresenter;

  public PersonalisationPageImpl() {

    this.previewPane = new GridPane();
    this.personalisationPage = new GridPane();
    personalisationPage.setVgap(20);
    personalisationPage.setHgap(20);
    personalisationPage.setPadding(new Insets(20));

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(100);
    personalisationPage.getColumnConstraints().addAll(col1);

    Label lblPers = new Label("Personalisation");
    Font font2 = Font.font("Arial", FontWeight.NORMAL, 20);
    lblPers.setFont(font2);
    GridPane.setFillWidth(lblPers, true);
    lblPers.setMaxWidth(Double.MAX_VALUE);
    lblPers.setAlignment(Pos.CENTER);

    personalisationPage.addRow(0, lblPers);
    personalisationPage.addRow(1, colourSettingsPane());
    personalisationPage.addRow(2, previewPane);
  }

  @Override
  public Pane colourSettingsPane() {
    ColorPicker backgroundColorPicker = new ColorPicker(Color.web("#364966"));
    ColorPicker textColorPicker = new ColorPicker(Color.WHITE);
    ColorPicker buttonColorPicker = new ColorPicker(Color.web("#dc3d4b"));
    ColorPicker highlightColorPicker = new ColorPicker(Color.web("#b78460"));

    GridPane colorSettingsPane = new GridPane();
    colorSettingsPane.setAlignment(Pos.CENTER);
    colorSettingsPane.setHgap(10);
    colorSettingsPane.setVgap(10);
    colorSettingsPane.addRow(0, new Label("Background color:"), backgroundColorPicker);
    colorSettingsPane.addRow(1, new Label("Text color:"), textColorPicker);
    colorSettingsPane.addRow(2, new Label("Button color:"), buttonColorPicker);
    colorSettingsPane.addRow(3, new Label("Highlight color:"), highlightColorPicker);

    backgroundColorPicker
        .valueProperty()
        .addListener(
            (observable, oldColor, newColor) ->
                updateBackgroundColorString(backgroundColorPicker.getValue()));
    textColorPicker
        .valueProperty()
        .addListener(
            (observable, oldColor, newColor) -> updateTextColorString(textColorPicker.getValue()));
    buttonColorPicker
        .valueProperty()
        .addListener(
            (observable, oldColor, newColor) ->
                updateButtonColorString(buttonColorPicker.getValue()));

    highlightColorPicker
        .valueProperty()
        .addListener(
            (observable, oldColor, newColor) ->
                updateHighlightColorString(highlightColorPicker.getValue()));

    return colorSettingsPane;
  }

  @Override
  public void buildPreviewPane(String highlightColor) {
    previewPane.getChildren().clear();
    Label lbl = new Label("The quick brown fox jumps over the lazy dog");
    lbl.setWrapText(true);
    lbl.setOnMouseEntered((event) -> lbl.setStyle(highlightColor));
    lbl.setOnMouseExited((event) -> lbl.setStyle("-fx-background-color: TRANSPARENT"));

    Button btn = new Button("Test");
    VBox vBox = new VBox(lbl, btn);
    previewPane.setAlignment(Pos.CENTER);

    previewPane.getChildren().add(vBox);
    previewPane.setPrefWidth(300);
  }

  @Override
  public void updateBackgroundColorString(Color backgroundColor) {
    String colorString = "-fx-background-color: " + toHexString(backgroundColor) + ";";
    displayPresenter.updateBackgroundColor(colorString);
  }

  @Override
  public void updateTextColorString(Color textColor) {
    String colorString = "-fx-text-background-color: " + toHexString(textColor) + ";";
    displayPresenter.updateTextColor(colorString);
  }

  @Override
  public void updateButtonColorString(Color buttonColor) {
    String colorString = "-fx-base: " + toHexString(buttonColor) + ";";
    displayPresenter.updateButtonColor(colorString);
  }

  @Override
  public void updateHighlightColorString(Color highlightColor) {
    String colorString = "-fx-background-color: " + toHexString(highlightColor) + ";";
    displayPresenter.updateHighlightColor(colorString);
  }

  @Override
  public Pane getPersonalisationPage() {
    return personalisationPage;
  }

  @Override
  public void setDisplayPresenter(DisplayPresenterImpl displayPresenter) {
    this.displayPresenter = displayPresenter;
  }
}
