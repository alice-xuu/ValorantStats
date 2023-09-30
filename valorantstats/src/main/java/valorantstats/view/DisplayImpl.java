package valorantstats.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import valorantstats.model.HistoryItem;
import valorantstats.presenter.DisplayPresenterImpl;
import org.controlsfx.control.ToggleSwitch;

import java.awt.image.BufferedImage;
import java.util.List;

/** Manages the main view of the application */
public class DisplayImpl implements Display {

  private final BorderPane mainPane;
  private final ScrollPane scrollPane;
  private final SplashPage splashPage;
  private final LoginPage loginPage;
  private final PersonalisationPageImpl personalisationPage;
  private final WitDisplay witDisplay;
  private final String textStyle;
  private SearchPage searchPage;

  private Scene scene;
  private GridPane homePane;
  private GridPane uploadImagePane;
  private GridPane aboutPane;
  private GridPane menuBar;
  private FlowPane bcb = new FlowPane();
  private int width = 640;
  private int height = 480;
  private Button btnUploadImage;
  private DisplayPresenterImpl displayPresenter;
  private Image imgHistoryArrow;
  private ToggleSwitch toggleSwitchFastMode;

  public DisplayImpl(
      SplashPage splashPage,
      LoginPage loginPage,
      PersonalisationPageImpl personalisationPage,
      SearchPage searchPage,
      WitDisplay witDisplay) {

    this.mainPane = new BorderPane();
    this.scrollPane = new ScrollPane();
    mainPane.setMinHeight(480);

    scrollPane.setContent(mainPane);
    scrollPane.setFitToWidth(true);
    // scrollPane.setFitToHeight(true);

    this.splashPage = splashPage;
    this.loginPage = loginPage;
    this.personalisationPage = personalisationPage;
    this.searchPage = searchPage;

    this.witDisplay = witDisplay;

    buildMenu();
    buildBreadCrumbBar();
    buildHomePane();

    mainPane.setStyle(
        "-fx-background-color: #364966;"
            + "-fx-base: #dc3d4b;"
            + "-fx-text-background-color: #ffffff;"
            + "-fx-font-size: 16;"
            + "-fx-font-family: Arial;");

    this.textStyle = "-fx-font-size: 16;" + "-fx-font-family: Arial;";

    imgHistoryArrow = new Image("Arrow-right.png", 15, 15, false, false, true);
  }

  @Override
  public void displayScene() {
    this.scene = new Scene(scrollPane, width, height);
  }

  @Override
  public void setDisplayPresenter(DisplayPresenterImpl displayPresenter) {
    this.displayPresenter = displayPresenter;
  }

  @Override
  public void showSplashPage() {
    mainPane.getChildren().clear();
    mainPane.setCenter(splashPage.getPane());
    splashPage.display();
    splashPage.startTimeline();
  }

  @Override
  public void showLoginPage() {
    mainPane.getChildren().clear();
    mainPane.setCenter(loginPage.getMainLoginPane());
  }

  @Override
  public void buildMenu() {
    Menu home = new Menu("Home");
    MenuItem homeItm = new MenuItem("Home");
    homeItm.setOnAction((event) -> displayPresenter.showHomePage());
    MenuItem aboutItm = new MenuItem("About");
    aboutItm.setOnAction((event) -> displayPresenter.showAboutPage());

    MenuItem personaliseItm = new MenuItem("Personalise");
    personaliseItm.setOnAction((event) -> displayPresenter.showPersonalisationPage());

    home.getItems().addAll(homeItm, aboutItm, personaliseItm);

    Menu search = new Menu("Search");
    MenuItem searchTeamItm = new MenuItem("Search team");
    searchTeamItm.setOnAction(
        (event) -> {
          displayPresenter.showSearchPage();
        });

    search.getItems().addAll(searchTeamItm);

    Menu fastMode = new Menu("Fast Mode");
    MenuItem fastModeOnItm = new MenuItem("Turn On");
    fastModeOnItm.setOnAction(
        (event) -> {
          displayPresenter.setFastMode(true, false);
        });

    MenuItem fastModeOffItm = new MenuItem("Turn Off");
    fastModeOffItm.setOnAction(
        (event) -> {
          displayPresenter.setFastMode(false, false);
        });

    fastMode.getItems().addAll(fastModeOnItm, fastModeOffItm);

    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().addAll(home, search, fastMode);

    buildTopBarMenu(menuBar);
  }

  @Override
  public void buildTopBarMenu(MenuBar menuBar) {
    toggleSwitchFastMode = new ToggleSwitch();
    toggleSwitchFastMode.setText("Fast Mode: Off");
    toggleSwitchFastMode.setOnMousePressed(
        (event -> {
          displayPresenter.setFastMode(!toggleSwitchFastMode.isSelected(), true);
        }));

    this.menuBar = new GridPane();
    this.menuBar.setHgap(10);
    ColumnConstraints col0 = new ColumnConstraints();
    col0.setHgrow(Priority.ALWAYS);
    this.menuBar.getColumnConstraints().add(col0);

    this.menuBar.getChildren().addAll(menuBar, toggleSwitchFastMode);

    GridPane.setColumnIndex(menuBar, 0);
    GridPane.setColumnIndex(toggleSwitchFastMode, 1);
  }

  @Override
  public void setSearchPage(SearchPage searchPage) {
    this.searchPage = searchPage;
  }

  @Override
  public void updateFastModeSwitch(String text, boolean mode, boolean isSwitch) {
    this.toggleSwitchFastMode.setText(text);
    if (!isSwitch) {
      this.toggleSwitchFastMode.setSelected(mode);
    }
  }

  @Override
  public void buildBreadCrumbBar() {
    bcb = new FlowPane();
  }

  @Override
  public void buildHomePane() {
    homePane = new GridPane();
    homePane.setVgap(20);
    homePane.setHgap(20);
    homePane.setPadding(new Insets(20));
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(100);
    homePane.getColumnConstraints().addAll(col1);

    Label lblHome = new Label("Home");
    Font font2 = Font.font("Arial", FontWeight.NORMAL, 20);
    lblHome.setFont(font2);
    GridPane.setFillWidth(lblHome, true);
    lblHome.setMaxWidth(Double.MAX_VALUE);
    lblHome.setAlignment(Pos.CENTER);

    homePane.addRow(0, lblHome);
    uploadImagePane = new GridPane();
    uploadImagePane.setVgap(20);
    homePane.addRow(1, uploadImagePane);
  }

  @Override
  public void buildUploadImagePane() {
    uploadImagePane.getChildren().clear();

    Label lblUpload =
        new Label(
            "Create a QR Code out of your team and match history and upload it to Imgur (Option will be disabled while you have no history)");
    lblUpload.setWrapText(true);

    btnUploadImage = new Button("Upload QR Code");
    uploadImagePane.addRow(1, lblUpload);
    uploadImagePane.addRow(2, btnUploadImage);
    btnUploadImage.setOnAction((event) -> displayPresenter.uploadImageAction());
  }

  @Override
  public void setDisableUploadImageButton(boolean disable) {
    btnUploadImage.setDisable(disable);
  }

  @Override
  public void displayUploadImageError(String report, BufferedImage bi, String errorMsg) {
    Label lblShortReportResult = new Label(report);
    lblShortReportResult.setWrapText(true);

    ImageView imgViewQR = new ImageView();
    imgViewQR.setImage(SwingFXUtils.toFXImage(bi, null));
    Label postResult = new Label(errorMsg);
    VBox vBox = new VBox(lblShortReportResult, imgViewQR, postResult);
    vBox.setSpacing(10);
    uploadImagePane.addRow(3, vBox);
  }

  @Override
  public void displayUploadImageSuccess(String report, BufferedImage bi, String link) {
    Label lblShortReportResult = new Label(report);
    lblShortReportResult.setWrapText(true);
    lblShortReportResult.setPrefWidth(getWidth());

    ImageView imgViewQR = new ImageView();
    imgViewQR.setImage(SwingFXUtils.toFXImage(bi, null));
    Label postResult = new Label("Upload successful");
    TextField tfImgurLink = new TextField(link);
    tfImgurLink.setVisible(true);
    VBox vBox = new VBox(lblShortReportResult, imgViewQR, postResult, tfImgurLink);
    vBox.setSpacing(10);
    uploadImagePane.addRow(3, vBox);
  }

  @Override
  public void buildAboutPane(String appName, String name, String references) {
    aboutPane = new GridPane();
    aboutPane.setVgap(20);
    aboutPane.setHgap(20);
    aboutPane.setPadding(new Insets(20));
    ColumnConstraints col1 = new ColumnConstraints();
    col1.setPercentWidth(100);
    aboutPane.getColumnConstraints().addAll(col1);

    Label lblAbout = new Label("About");
    Font font2 = Font.font("Arial", FontWeight.NORMAL, 20);
    lblAbout.setFont(font2);
    GridPane.setFillWidth(lblAbout, true);
    lblAbout.setMaxWidth(Double.MAX_VALUE);
    lblAbout.setAlignment(Pos.CENTER);

    Label lblAppName = new Label("Application name: " + appName);
    Label lblName = new Label("Name: " + name);
    Label lblReferences = new Label("References: \n" + references);
    lblReferences.setWrapText(true);

    aboutPane.addRow(0, lblAbout);
    aboutPane.addRow(1, lblAppName);
    aboutPane.addRow(2, lblName);
    aboutPane.addRow(3, lblReferences);
  }

  @Override
  public void activateBtnUploadImage() {
    btnUploadImage.fire();
  }

  /** Updates the breadcrumb bar and colors */
  @Override
  public void update(List<HistoryItem> historyList, String colorString) {

    bcb.getChildren().clear();

    if (historyList.size() > 1) {
      Button buttonBack = new Button("Back");
      bcb.getChildren().add(buttonBack);
      buttonBack.setOnAction((event) -> displayPresenter.goBackAction());
    }
    for (int i = 0; i < historyList.size(); i++) {
      HistoryItem historyItem = historyList.get(i);
      Button buttonHistoryItem = new Button(historyItem.getSearchedItem());
      if (i != 0) {
        ImageView imgViewHistoryArrow = new ImageView(imgHistoryArrow);
        bcb.getChildren().addAll(imgViewHistoryArrow, buttonHistoryItem);
      } else {
        bcb.getChildren().addAll(buttonHistoryItem);
      }

      int finalIndex = i;
      buttonHistoryItem.setOnAction(
          (event) -> displayPresenter.breadcrumbClickAction(historyItem, finalIndex));
    }

    mainPane.setStyle(colorString + textStyle);
  }

  @Override
  public void homeAction() {
    buildHomePane();

    mainPane.getChildren().clear();
    VBox vBoxMenuBcb = new VBox(menuBar, bcb);
    mainPane.setTop(vBoxMenuBcb);
    mainPane.setCenter(homePane);
    mainPane.setBottom(witDisplay.getWitDisplay());
  }

  @Override
  public void aboutAction() {
    mainPane.getChildren().clear();
    VBox vBoxMenuBcb = new VBox(menuBar, bcb);
    mainPane.setTop(vBoxMenuBcb);
    mainPane.setCenter(aboutPane);
    mainPane.setBottom(witDisplay.getWitDisplay());
  }

  @Override
  public void searchTeamAction() {
    mainPane.getChildren().clear();
    // searchPage.clearPane();
    VBox vBoxMenuBcb = new VBox(menuBar, bcb);
    mainPane.setTop(vBoxMenuBcb);
    mainPane.setCenter(searchPage.getSearchPane());
    mainPane.setBottom(witDisplay.getWitDisplay());
  }

  @Override
  public void personalisationAction() {
    mainPane.getChildren().clear();
    VBox vBoxMenuBcb = new VBox(menuBar, bcb);
    mainPane.setTop(vBoxMenuBcb);
    mainPane.setCenter(personalisationPage.getPersonalisationPage());
    mainPane.setBottom(witDisplay.getWitDisplay());
  }

  @Override
  public Scene getScene() {
    return this.scene;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public BorderPane getMainPane() {
    return mainPane;
  }
}
