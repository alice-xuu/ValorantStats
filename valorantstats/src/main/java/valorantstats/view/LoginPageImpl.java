package valorantstats.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import valorantstats.presenter.DisplayPresenterImpl;

import static javafx.scene.layout.GridPane.REMAINING;
import static javafx.scene.layout.GridPane.setColumnSpan;

public class LoginPageImpl implements LoginPage {
  private final BorderPane mainLoginPane;
  private GridPane loginChoicePane;
  private Font fontBold;
  private DisplayPresenterImpl displayPresenter;

  public LoginPageImpl() {

    this.mainLoginPane = new BorderPane();

    mainLoginPane.setMaxWidth(Double.MAX_VALUE);

    this.loginChoicePane = new GridPane();
    loginChoicePane.setPadding(new Insets(30));
    loginChoicePane.setHgap(30);
    loginChoicePane.setVgap(30);

    fontBold = Font.font("Arial", FontWeight.NORMAL, 20);

    Label lblWelcome = new Label("Welcome to Pandascore Valorant");
    Font font = Font.font("Arial", FontWeight.BOLD, 30);
    lblWelcome.setFont(font);

    Label lblLoginChoice =
        new Label("Would you like to login to an existing account or create a new account?");
    lblLoginChoice.setWrapText(true);
    lblLoginChoice.setFont(fontBold);

    Button btnExistingAcc = new Button("Existing Account");
    btnExistingAcc.setFont(fontBold);
    Button btnNewAcc = new Button("New Account");
    btnNewAcc.setFont(fontBold);

    btnExistingAcc.setOnAction(
        (event) -> {
          displayPresenter.showExistingAccountPage();
        });
    btnNewAcc.setOnAction(
        (event) -> {
          displayPresenter.showNewAccountPage();
        });

    setColumnSpan(lblWelcome, REMAINING);
    GridPane.setFillWidth(lblWelcome, true);
    lblWelcome.setMaxWidth(Double.MAX_VALUE);
    lblWelcome.setAlignment(Pos.CENTER);

    setColumnSpan(lblLoginChoice, REMAINING);
    GridPane.setFillWidth(lblLoginChoice, true);
    lblLoginChoice.setMaxWidth(Double.MAX_VALUE);
    lblLoginChoice.setAlignment(Pos.CENTER);
    lblLoginChoice.setTextAlignment(TextAlignment.CENTER);

    HBox hBox = new HBox(btnExistingAcc, btnNewAcc);
    hBox.setSpacing(30);
    GridPane.setFillWidth(hBox, true);
    hBox.setMaxWidth(Double.MAX_VALUE);
    hBox.setAlignment(Pos.CENTER);

    Line line = new Line();
    line.setStartX(50f);
    line.setEndX(590f);
    line.setStrokeWidth(3);
    line.setStyle("-fx-stroke: #fa4454;");

    loginChoicePane.addRow(0, lblWelcome);
    loginChoicePane.addRow(2, line);
    loginChoicePane.addRow(3, lblLoginChoice);
    loginChoicePane.addRow(4, hBox);

    loginChoicePane.setAlignment(Pos.CENTER);
    loginChoicePane.setPrefWidth(300);

    mainLoginPane.setCenter(loginChoicePane);
  }

  // log in to an existing account
  @Override
  public void existingAccount() {
    GridPane existingAccountPane = new GridPane();
    existingAccountPane.setPadding(new Insets(30));
    existingAccountPane.setHgap(30);
    existingAccountPane.setVgap(30);

    Label lblUsername = new Label("Username");
    lblUsername.setFont(fontBold);

    TextField tfUsername = new TextField();
    tfUsername.setFont(fontBold);

    Label lblPassword = new Label("Password");
    lblPassword.setFont(fontBold);

    TextField tfPassword = new TextField();
    tfPassword.setFont(fontBold);

    Button btnSubmit = new Button("Submit");
    btnSubmit.setFont(fontBold);

    btnSubmit.setOnAction(
        event -> {
          displayPresenter.existingAccountSubmitAction(tfUsername.getText(), tfPassword.getText());
        });

    Label lblTitle = new Label("Enter your existing account details");
    lblTitle.setFont(fontBold);

    setColumnSpan(lblTitle, REMAINING);
    existingAccountPane.add(lblTitle, 0, 0);

    existingAccountPane.add(lblUsername, 0, 1);
    existingAccountPane.add(tfUsername, 1, 1);

    existingAccountPane.add(lblPassword, 0, 2);
    existingAccountPane.add(tfPassword, 1, 2);

    existingAccountPane.add(createBackButton(), 0, 3);
    existingAccountPane.add(btnSubmit, 1, 3);

    existingAccountPane.setAlignment(Pos.CENTER);
    mainLoginPane.setCenter(existingAccountPane);
  }

  // create a new account
  @Override
  public void newAccount() {
    GridPane newAccountPane = new GridPane();
    newAccountPane.setPadding(new Insets(30));
    newAccountPane.setHgap(30);
    newAccountPane.setVgap(30);

    Label lblUsername = new Label("Username");
    lblUsername.setFont(fontBold);

    TextField tfUsername = new TextField();
    tfUsername.setFont(fontBold);

    Label lblPassword = new Label("Password");
    lblPassword.setFont(fontBold);

    TextField tfPassword = new TextField();
    tfPassword.setFont(fontBold);

    Button btnSubmit = new Button("Submit");
    btnSubmit.setFont(fontBold);

    btnSubmit.setOnAction(
        (event -> {
          displayPresenter.newAccountSubmitAction(tfUsername.getText(), tfPassword.getText());
        }));

    Label lblTitle = new Label("Enter details for your new account");
    lblTitle.setFont(fontBold);

    setColumnSpan(lblTitle, REMAINING);
    newAccountPane.add(lblTitle, 0, 0);

    newAccountPane.add(lblUsername, 0, 1);
    newAccountPane.add(tfUsername, 1, 1);

    newAccountPane.add(lblPassword, 0, 2);
    newAccountPane.add(tfPassword, 1, 2);

    newAccountPane.add(createBackButton(), 0, 3);
    newAccountPane.add(btnSubmit, 1, 3);

    newAccountPane.setAlignment(Pos.CENTER);
    mainLoginPane.setCenter(newAccountPane);
  }

  /**
   * Back button returns to the screen where the user can choose existing or new account
   *
   * @return back button
   */
  @Override
  public Button createBackButton() {
    Button btnBack = new Button("Back");
    btnBack.setFont(fontBold);

    btnBack.setOnAction(
        event -> {
          displayPresenter.showLoginChoicePage();
        });
    return btnBack;
  }

  @Override
  public void loginChoice() {
    mainLoginPane.setCenter(loginChoicePane);
  }

  @Override
  public Pane getMainLoginPane() {
    return mainLoginPane;
  }

  @Override
  public void accountAlert(String errorMsg) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Account Error");
    alert.setHeaderText(errorMsg);
    alert.showAndWait();
  }

  @Override
  public void setDisplayPresenter(DisplayPresenterImpl displayPresenter) {
    this.displayPresenter = displayPresenter;
  }
}
