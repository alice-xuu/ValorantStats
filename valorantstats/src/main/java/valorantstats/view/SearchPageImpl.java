package valorantstats.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Player;
import valorantstats.model.jsonobjects.Team;
import valorantstats.presenter.SearchPagePresenter;

import java.util.AbstractMap.SimpleEntry;
import java.util.Optional;

import static javafx.scene.layout.GridPane.REMAINING;
import static javafx.scene.layout.GridPane.setColumnSpan;

public class SearchPageImpl implements SearchPage {

  private final GridPane searchPane;
  private final GridPane resultsPane;

  private final String noImage = "No_image_available.svg.png";
  private SearchPagePresenter searchPagePresenter;
  private Button btnClearCache;
  private Button btnSearch;
  private TextField tfSearch;

  public SearchPageImpl() {
    this.searchPane = new GridPane();
    searchPane.setPadding(new Insets(20));

    this.resultsPane = new GridPane();
    resultsPane.setVgap(15);
    resultsPane.setHgap(15);

    Label labelSearch = new Label("Enter a team:");
    Font font = Font.font("Arial", FontWeight.BOLD, 20);
    labelSearch.setFont(font);

    tfSearch = new TextField();
    btnSearch = new Button();
    btnSearch.setText("Search");
    btnSearch.setOnAction(
        (event) -> {
          searchPagePresenter.searchTeamInput(tfSearch.getText(), false);
        });

    btnClearCache = new Button();
    btnClearCache.setText("Clear Cache");
    btnClearCache.setOnAction(
        (event) -> {
          searchPagePresenter.clearCache();
        });

    HBox hbSearch = new HBox();
    hbSearch.getChildren().addAll(labelSearch, tfSearch, btnSearch, btnClearCache);

    hbSearch.setSpacing(10);

    searchPane.add(hbSearch, 0, 1);
    searchPane.add(resultsPane, 0, 3);
  }

  @Override
  public void clearSearchTextField() {
    tfSearch.clear();
  }

  @Override
  public void setBtnSearch(boolean disable) {
    btnSearch.setDisable(disable);
  }

  @Override
  public void setBtnClearCacheDisable(boolean disable) {
    btnClearCache.setDisable(disable);
  }

  @Override
  public void searchTeam(String name) {
    searchPagePresenter.searchTeamInput(name, false);
  }

  @Override
  public void processSearchTeamCacheHit(String name, SimpleEntry<CallResult, String> teamResult) {
    ButtonType cache = new ButtonType("Cache", ButtonBar.ButtonData.NO);
    ButtonType fresh = new ButtonType("Fresh", ButtonBar.ButtonData.YES);
    Alert alert = alertCache(cache, fresh);
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == cache) {
      searchPagePresenter.teamCacheOptionAction(name, teamResult);
    } else if (result.isPresent() && result.get() == fresh) {
      searchPagePresenter.searchTeamInput(name, true);
    }
  }

  @Override
  public void processSearchMatchCacheHit(String name, SimpleEntry<CallResult, String> teamResult) {
    ButtonType cache = new ButtonType("Cache", ButtonBar.ButtonData.NO);
    ButtonType fresh = new ButtonType("Fresh", ButtonBar.ButtonData.YES);
    Alert alert = alertCache(cache, fresh);
    Optional<ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == cache) {
      searchPagePresenter.matchCacheOptionAction(name, teamResult);
    } else if (result.isPresent() && result.get() == fresh) {
      searchPagePresenter.runSearchMatchTask(name, true);
    }
  }

  @Override
  public void displaySearchTeamError(SimpleEntry<CallResult, String> searchTeamResult) {
    clearPane();
    searchAlert(searchTeamResult.getValue());
  }

  @Override
  public void displaySearchTeam(Team team, String highlightColor) {
    resultsPane.getChildren().clear();

    Label lblName = new Label(team.getName());
    Font font = Font.font("Arial", FontWeight.BOLD, 24);
    lblName.setFont(font);
    lblName.setUnderline(true);

    lblName.setOnMouseEntered((event) -> highlight(lblName, highlightColor));
    lblName.setOnMouseExited((event) -> endHighlight(lblName));
    lblName.setOnMouseClicked(
        (event) -> {
          searchPagePresenter.runSearchMatchTask(team.getId().toString(), false);
        });

    Image img = null;
    if (team.getImage_url() == null) {
      img = new Image(noImage, 200, 200, false, false, true);
    } else {
      img = new Image(team.getImage_url(), 200, 200, false, false, true);
    }
    ImageView imgView = new ImageView(img);
    Label lblTeamInfo = new Label("Team Information:");
    Label lblLoc = new Label("Location: " + team.getLocation() + "\nAcronym: " + team.getAcronym());
    VBox teamInfo = new VBox();
    teamInfo.setSpacing(10);
    teamInfo.getChildren().addAll(lblName, lblTeamInfo, lblLoc);
    resultsPane.add(imgView, 0, 1, 2, 2);
    resultsPane.add(teamInfo, 2, 1, 2, 2);

    Label lblPlayers = new Label("Players");
    Font fontPlayers = Font.font("Arial", FontWeight.BOLD, 20);
    lblPlayers.setFont(fontPlayers);
    setColumnSpan(lblPlayers, REMAINING);
    GridPane.setFillWidth(lblPlayers, true);
    lblPlayers.setMaxWidth(Double.MAX_VALUE);
    lblPlayers.setAlignment(Pos.CENTER);

    resultsPane.add(lblPlayers, 0, 3);

    Label lblColumnImg = new Label("Image");
    Label lblColumnAlias = new Label("Alias");
    Label lblColumnFirstName = new Label("First Name");
    Label lblColumnLastName = new Label("Last Name");
    Label lblColumnNationality = new Label("Nationality");

    resultsPane.addRow(
        4,
        lblColumnImg,
        lblColumnAlias,
        lblColumnFirstName,
        lblColumnLastName,
        lblColumnNationality);
    Font fontCol = Font.font("Arial", FontWeight.BOLD, 16);
    lblColumnImg.setFont(fontCol);
    lblColumnAlias.setFont(fontCol);
    lblColumnFirstName.setFont(fontCol);
    lblColumnLastName.setFont(fontCol);
    lblColumnNationality.setFont(fontCol);

    int index = 5;
    for (Player player : team.getPlayers()) {
      Label lblPlayerName = new Label(player.getName());
      Label lblPlayerFirstName = new Label(player.getFirst_name());
      Label lblPlayerLastName = new Label(player.getLast_name());
      Label lblPlayerNationality = new Label(player.getNationality());
      lblPlayerName.setUnderline(true);
      lblPlayerName.setOnMouseEntered((event) -> highlight(lblPlayerName, highlightColor));
      lblPlayerName.setOnMouseExited((event) -> endHighlight(lblPlayerName));
      lblPlayerName.setOnMouseClicked(
          (event) -> {
            handleTeamMatchesClick(team.getId().toString());
          });

      Image imgPlayer = null;
      if (player.getImage_url() == null) {
        imgPlayer = new Image(noImage, 150, 150, false, false, true);
      } else {
        imgPlayer = new Image(player.getImage_url(), 150, 150, false, false, true);
      }
      ImageView imgViewPlayer = new ImageView(imgPlayer);
      resultsPane.addRow(
          index,
          imgViewPlayer,
          lblPlayerName,
          lblPlayerFirstName,
          lblPlayerLastName,
          lblPlayerNationality);
      index++;
    }
  }

  @Override
  public void handleTeamMatchesClick(String name) {
    searchPagePresenter.runSearchMatchTask(name, false);
  }

  @Override
  public void displayHandleTeamMatchesClickError(
      SimpleEntry<CallResult, String> searchMatchResult) {
    clearPane();
    searchAlert(searchMatchResult.getValue());
  }

  @Override
  public void displayHandleTeamMatchesClick(Match[] matches, String highlightColor) {

    resultsPane.getChildren().clear();
    int index = 2;

    Label lblDateColumn = new Label("Date");
    lblDateColumn.setWrapText(true);
    lblDateColumn.setTextAlignment(TextAlignment.CENTER);

    Label lblNOGColumn = new Label("Number of games");
    lblNOGColumn.setWrapText(true);
    lblNOGColumn.setTextAlignment(TextAlignment.CENTER);

    Label lblOp1Column = new Label("Opponent 1");
    lblOp1Column.setWrapText(true);
    lblOp1Column.setTextAlignment(TextAlignment.CENTER);

    Label lblOp2Column = new Label("Opponent 2");
    lblOp2Column.setWrapText(true);
    lblOp2Column.setTextAlignment(TextAlignment.CENTER);

    Font font = Font.font("Arial", FontWeight.BOLD, 15);
    lblDateColumn.setFont(font);
    lblNOGColumn.setFont(font);
    lblOp1Column.setFont(font);
    lblOp2Column.setFont(font);

    resultsPane.addRow(1, lblDateColumn, lblNOGColumn, lblOp1Column, lblOp2Column);

    for (Match match : matches) {
      Label lblDate = new Label(match.getBegin_at());
      Label lblGames = new Label(match.getNumber_of_games().toString());

      Match.Opponent opponent1 = match.getOpponents().get(0);
      Match.Opponent opponent2 = match.getOpponents().get(1);
      Label lblOpponent1 = new Label(opponent1.getOpponent().getName());
      Label lblOpponent2 = new Label(opponent2.getOpponent().getName());
      lblOpponent1.setUnderline(true);
      lblOpponent2.setUnderline(true);

      lblOpponent1.setOnMouseEntered((event) -> highlight(lblOpponent1, highlightColor));
      lblOpponent1.setOnMouseExited((event) -> endHighlight(lblOpponent1));
      lblOpponent1.setOnMouseClicked(
          (event) -> {
            searchTeam(opponent1.getOpponent().getName());
          });

      lblOpponent2.setOnMouseEntered((event) -> highlight(lblOpponent2, highlightColor));
      lblOpponent2.setOnMouseExited((event) -> endHighlight(lblOpponent2));
      lblOpponent2.setOnMouseClicked(
          (event) -> {
            searchTeam(opponent2.getOpponent().getName());
          });

      resultsPane.addRow(index, lblDate, lblGames, lblOpponent1, lblOpponent2);

      index += 2;
    }
  }

  @Override
  public void clearPane() {
    resultsPane.getChildren().clear();
  }

  @Override
  public Alert alertCache(ButtonType cache, ButtonType fresh) {

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", cache, fresh);
    alert.setTitle("Cache Alert");
    alert.setHeaderText("Cache exists for this data");
    alert.setContentText("Would you like to retrieve the cache results or generate fresh results?");
    return alert;
  }

  @Override
  public GridPane getSearchPane() {
    return searchPane;
  }

  @Override
  public GridPane getResultsPane() {
    return resultsPane;
  }

  @Override
  public void searchAlert(String errorMsg) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Search Error");
    alert.setHeaderText(errorMsg);
    alert.showAndWait();
  }

  @Override
  public void showCacheAlert() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Cache Alert");
    alert.setHeaderText("Cache Cleared");
    alert.setContentText("All cached team and match results have been cleared");
    alert.showAndWait();
  }

  @Override
  public void setSearchPagePresenter(SearchPagePresenter searchPagePresenter) {
    this.searchPagePresenter = searchPagePresenter;
  }
}
