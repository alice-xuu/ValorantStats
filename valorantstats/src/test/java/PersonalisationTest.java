import valorantstats.model.*;
import valorantstats.model.apicalls.dummy.InputDummyApi;
import valorantstats.model.apicalls.dummy.OutputDummyApi;
import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PersonalisationTest {
  ModelFacade model;
  Database databaseMock;
  InputDummyApi inputDummyMock;
  OutputDummyApi outputDummyMock;

  @BeforeEach
  public void createBackendOffline() {
    databaseMock = mock(Database.class);
    inputDummyMock = mock(InputDummyApi.class);
    inputDummyMock = mock(InputDummyApi.class);
    outputDummyMock = mock(OutputDummyApi.class);

    model = new ModelFacade(false, false, databaseMock);

    model.getInputApiCalls().setInputDummyApi(inputDummyMock);
    model.getOutputApiCalls().setOutputDummyApi(outputDummyMock);
  }

  @Test
  public void createNewAccountValid() {
    String username = "name";
    String password = "password";
    when(databaseMock.queryUsername(username)).thenReturn(false);

    CallResult callResult = model.createNewAccount(username, password);
    assertEquals(CallResult.SUCCESS, callResult);
  }

  @Test
  public void createNewAccountInvalidUsername() {
    String username = "";
    String password = "password";
    CallResult callResult = model.createNewAccount(username, password);
    assertEquals(CallResult.ERROR, callResult);
  }

  @Test
  public void createNewAccountInvalidPassword() {
    String username = "name";
    String password = "";
    CallResult callResult = model.createNewAccount(username, password);
    assertEquals(CallResult.ERROR, callResult);
  }

  @Test
  public void createNewAccountUsernameExists() {
    String username = "name";
    String password = "password";
    when(databaseMock.queryUsername(username)).thenReturn(true);

    CallResult callResult = model.createNewAccount(username, password);
    assertEquals(CallResult.ERROR, callResult);
  }

  @Test
  public void verifyExistingAccountValid() {
    String username = "name";
    String password = "password";
    String hashedPassword = sha1Hex(password);

    when(databaseMock.queryAccount(username, hashedPassword)).thenReturn(true);

    CallResult callResult = model.verifyExistingAccount(username, password);
    assertEquals(CallResult.SUCCESS, callResult);
  }

  @Test
  public void verifyExistingAccountInvalid() {
    String username = "name";
    String password = "password";
    String hashedPassword = sha1Hex(password);

    when(databaseMock.queryAccount(username, hashedPassword)).thenReturn(false);

    CallResult callResult = model.verifyExistingAccount(username, password);
    assertEquals(CallResult.ERROR, callResult);
  }

  @Test
  public void updateComponentColors() {
    String colorString = "-fx-background-color: " + "TRANSPARENT" + ";";

    model.setCurrentUser("name");
    model.updateBackgroundColor(colorString);
    verify(databaseMock, times(1)).updateBackgroundColor("name", colorString);

    model.updateTextColor(colorString);
    verify(databaseMock, times(1)).updateTextColor("name", colorString);

    model.updateButtonColor(colorString);
    verify(databaseMock, times(1)).updateButtonColor("name", colorString);
  }

  @Test
  public void updateBreadcrumb() {
    model.setCurrentUser("name");
    model.storeBreadcrumb(model.breadCrumbString(), model.getCurrentHistoryIndex());
    verify(databaseMock, times(1))
        .updateBreadcrumb("name", model.breadCrumbString(), model.getCurrentHistoryIndex());
  }

  @Test
  public void getBreadcrumbIndex() {
    model.setCurrentUser("name");
    when(databaseMock.queryBreadcrumbIndex("name")).thenReturn(1);

    Integer index = model.getBreadcrumbIndex();
    assertEquals(1, index);
    verify(databaseMock, times(1)).queryBreadcrumbIndex("name");
  }

  @Test
  public void getBreadcrumbIndexZero() {
    model.setCurrentUser("name");
    when(databaseMock.queryBreadcrumbIndex("name")).thenReturn(0);

    Integer index = model.getBreadcrumbIndex();
    assertEquals(0, index);
    verify(databaseMock, times(1)).queryBreadcrumbIndex("name");
  }

  @Test
  public void getBreadcrumbDefault() {
    model.setCurrentUser("name");
    when(databaseMock.queryBreadcrumb("name")).thenReturn("");

    List<HistoryItem> list = model.retrieveBreadcrumb();

    List<HistoryItem> expected = new ArrayList<>();
    assertEquals(expected, list);
  }

  @Test
  public void getBreadcrumbTeamsMatches() {
    model.setCurrentUser("name");
    when(databaseMock.queryBreadcrumb("name"))
        .thenReturn(
            "TEAM--Acend--SUCCESS--result---TEAM--Sentinels--SUCCESS--result---TEAMMATCH--128715--SUCCESS--result");

    List<HistoryItem> list = model.retrieveBreadcrumb();

    List<HistoryItem> expected = new ArrayList<>();
    expected.add(
        new HistoryItem(SearchType.TEAM, "Acend", new SimpleEntry<>(CallResult.SUCCESS, "result")));
    expected.add(
        new HistoryItem(
            SearchType.TEAM, "Sentinels", new SimpleEntry<>(CallResult.SUCCESS, "result")));
    expected.add(
        new HistoryItem(
            SearchType.TEAMMATCH, "128715", new SimpleEntry<>(CallResult.SUCCESS, "result")));

    for (int i = 0; i < expected.size(); i++) {
      assertEquals(expected.get(i).getSearchType(), list.get(i).getSearchType());
    }
  }

  @Test
  public void getBreadcrumbTeamsMatchesTwoWordTeam() {
    model.setCurrentUser("name");
    when(databaseMock.queryBreadcrumb("name")).thenReturn("TEAM--Evil Geniuses--SUCCESS--result");

    List<HistoryItem> list = model.retrieveBreadcrumb();
    List<HistoryItem> expected = new ArrayList<>();
    expected.add(
        new HistoryItem(
            SearchType.TEAM, "Evil Geniuses", new SimpleEntry<>(CallResult.SUCCESS, "result")));
    assertEquals(expected.get(0).getSearchedItem(), list.get(0).getSearchedItem());
  }

  @Test
  public void getCurrentTeamOrMatchSuccess() {
    model.setCurrentUser("name");
    when(databaseMock.queryCall("name")).thenReturn(new SimpleEntry<>("SUCCESS", "api result"));

    SimpleEntry<CallResult, String> result = model.getCurrentTeamOrMatch();
    assertEquals(result.getKey(), CallResult.SUCCESS);
    assertEquals(result.getValue(), "api result");
  }

  @Test
  public void getCurrentTeamOrMatchError() {
    model.setCurrentUser("name");
    when(databaseMock.queryCall("name")).thenReturn(new SimpleEntry<>("ERROR", "error message"));

    SimpleEntry<CallResult, String> result = model.getCurrentTeamOrMatch();
    assertEquals(result.getKey(), CallResult.ERROR);
    assertEquals(result.getValue(), "error message");
  }

  @Test
  public void updateHighlightColor() {
    model.setCurrentUser("name");
    model.updateHighlightColor("colorString");
    verify(databaseMock, times(1)).updateHighlightColors("name", "colorString");
  }

  @Test
  public void getBackgroundColorAndHighlightColor() {
    model.setCurrentUser("name");
    when(databaseMock.queryComponentColor("name", "backgroundColor"))
        .thenReturn("background color");
    when(databaseMock.queryHighlightColors("name")).thenReturn("highlight color");

    String colors = model.getComponentColor("backgroundColor");
    String highlightColor = model.getHighlightColor();
    assertEquals("background color", colors);
    assertEquals("highlight color", highlightColor);

    verify(databaseMock, times(1)).queryComponentColor("name", "backgroundColor");
    verify(databaseMock, times(1)).queryHighlightColors("name");
  }

  @Test
  public void getComponentColors() {
    model.setCurrentUser("name");
    when(databaseMock.queryComponentColor("name", "textColor")).thenReturn("text color");
    String colors = model.getComponentColor("textColor");
    assertEquals("text color", colors);
    verify(databaseMock, times(1)).queryComponentColor("name", "textColor");

    when(databaseMock.queryComponentColor("name", "buttonColor")).thenReturn("button color");
    String colors2 = model.getComponentColor("buttonColor");
    assertEquals("button color", colors2);
    verify(databaseMock, times(1)).queryComponentColor("name", "buttonColor");
  }

  @Test
  public void updateCurrentTeamOrMatch() {
    model.setCurrentUser("name");

    SimpleEntry<CallResult, String> currentTeamOrMatch =
        new SimpleEntry<>(CallResult.SUCCESS, "result");
    model.updateCurrentTeamOrMatch(currentTeamOrMatch);
    verify(databaseMock, times(1))
        .updateCurrentTeamOrMatch(
            "name", String.valueOf(currentTeamOrMatch.getKey()), currentTeamOrMatch.getValue());
  }

  @Test
  public void updateUserStateDB() {
    model.setCurrentUser("name");

    String breadcrumb = "some breadcrumb";
    int breadcrumbIndex = 1;
    SimpleEntry<CallResult, String> currentTeamOrMatch =
        new SimpleEntry<>(CallResult.SUCCESS, "result");

    model.updateUserStateDB(breadcrumb, breadcrumbIndex, currentTeamOrMatch);
    verify(databaseMock, times(1))
        .updateCurrentTeamOrMatch(
            "name", String.valueOf(currentTeamOrMatch.getKey()), currentTeamOrMatch.getValue());
    verify(databaseMock, times(1)).updateBreadcrumb("name", breadcrumb, breadcrumbIndex);
  }
}
