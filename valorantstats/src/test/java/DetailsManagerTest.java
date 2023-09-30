import valorantstats.model.Database;
import valorantstats.model.HistoryItem;
import valorantstats.model.ModelFacade;
import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.apicalls.OutputApiCalls;
import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class DetailsManagerTest {

  ModelFacade model;
  Database databaseMock;
  InputApiCalls inputApiCallsMock;
  OutputApiCalls outputApiCallsMock;

  @BeforeEach
  public void createBackendOnline() {
    databaseMock = mock(Database.class);
    inputApiCallsMock = mock(InputApiCalls.class);
    outputApiCallsMock = mock(OutputApiCalls.class);

    model = new ModelFacade(true, true, databaseMock);

    model.setInputApiCalls(inputApiCallsMock);
    model.setOutputApiCalls(outputApiCallsMock);
    model.getApiCaller().setInputApiCalls(inputApiCallsMock);
  }

  @Test
  public void historyAdd() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    assertEquals(SearchType.TEAM, model.getHistory().get(0).getSearchType());
    assertEquals("Sentinels", model.getHistory().get(0).getSearchedItem());
    assertEquals(CallResult.SUCCESS, model.getHistory().get(0).getSearchResult().getKey());
    assertEquals("result", model.getHistory().get(0).getSearchResult().getValue());
  }

  @Test
  public void historyAddMatches() {
    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    model.addHistory(
        SearchType.TEAMMATCH, "123", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));

    assertEquals(SearchType.TEAM, model.getHistory().get(0).getSearchType());
    assertEquals("Sentinels", model.getHistory().get(0).getSearchedItem());
    assertEquals(SearchType.TEAMMATCH, model.getHistory().get(1).getSearchType());
    assertEquals("123", model.getHistory().get(1).getSearchedItem());
  }

  @Test
  public void setHistorySingleItem() {

    List<HistoryItem> expected = new ArrayList<>();
    expected.add(
        new HistoryItem(
            SearchType.TEAM, "name", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result")));

    model.setHistory(expected);
    assertEquals(expected, model.getHistory());
  }

  @Test
  public void setHistoryMultiItem() {

    List<HistoryItem> expected = new ArrayList<>();
    expected.add(
        new HistoryItem(
            SearchType.TEAM, "name", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result")));
    expected.add(
        new HistoryItem(
            SearchType.TEAMMATCH, "id", new AbstractMap.SimpleEntry<>(CallResult.ERROR, "result")));

    model.setHistory(expected);
    assertEquals(expected, model.getHistory());
  }

  // history starts empty, back returns null
  @Test
  public void backHistory() {

    HistoryItem result = model.getBackHistory();
    assertNull(result);
  }

  // back past multiple times
  @Test
  public void backHistoryPast() {

    HistoryItem result = model.getBackHistory();
    assertNull(result);

    HistoryItem result2 = model.getBackHistory();
    assertNull(result2);

    HistoryItem result3 = model.getBackHistory();
    assertNull(result3);
  }

  @Test
  public void backHistoryAddOne() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    HistoryItem result = model.getBackHistory();
    assertEquals(SearchType.TEAM, result.getSearchType());
    assertEquals("Sentinels", result.getSearchedItem());
  }

  @Test
  public void backHistoryAddTwoBackOne() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    model.addHistory(
        SearchType.TEAM, "BOBO", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));

    HistoryItem result = model.getBackHistory();
    assertEquals(SearchType.TEAM, result.getSearchType());
    assertEquals("Sentinels", result.getSearchedItem());
  }

  // history goes to first index (0) if history list isn't empty
  @Test
  public void backHistoryAddTwoBackTwo() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    model.addHistory(
        SearchType.TEAM, "BOBO", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));

    HistoryItem result = model.getBackHistory();
    assertEquals(SearchType.TEAM, result.getSearchType());
    assertEquals("Sentinels", result.getSearchedItem());

    HistoryItem result2 = model.getBackHistory();
    assertEquals(SearchType.TEAM, result2.getSearchType());
    assertEquals("Sentinels", result2.getSearchedItem());
  }

  // home + search 1 + search 2 -> go back to search 1 -> search 3 (index should go to last in list)
  // -> go back to search 2
  @Test
  public void backHistoryAfterTwoAddHistory() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    model.addHistory(
        SearchType.TEAM, "BOBO", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));

    HistoryItem result = model.getBackHistory();
    assertEquals(SearchType.TEAM, result.getSearchType());
    assertEquals("Sentinels", result.getSearchedItem());

    model.addHistory(
        SearchType.TEAM, "ACEND", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));

    HistoryItem result2 = model.getBackHistory();
    assertEquals(SearchType.TEAM, result2.getSearchType());
    assertEquals("BOBO", result2.getSearchedItem());
  }

  @Test
  public void shortFormReportString() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    assertEquals("TEAM:Sentinels", model.shortFormReportString());
  }

  @Test
  public void shortFormReportStringMultiple() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    model.addHistory(
        SearchType.TEAM, "BOBO", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    model.addHistory(
        SearchType.TEAM, "Acend", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));

    assertEquals(2, model.getCurrentHistoryIndex());
    assertEquals("TEAM:Sentinels, TEAM:BOBO, TEAM:Acend", model.shortFormReportString());
  }

  @Test
  public void breadcrumbReportString() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    assertEquals("TEAM--Sentinels--SUCCESS--result", model.breadCrumbString());
  }

  @Test
  public void breadcrumbReportStringMultiple() {

    model.addHistory(
        SearchType.TEAM, "Sentinels", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));
    model.addHistory(
        SearchType.TEAM, "BOBO", new AbstractMap.SimpleEntry<>(CallResult.SUCCESS, "result"));

    assertEquals(
        "TEAM--Sentinels--SUCCESS--result---TEAM--BOBO--SUCCESS--result", model.breadCrumbString());
  }

  @Test
  public void getAppInfo() {
    assertNotNull(model.getAppName());
    assertNotNull(model.getName());
    assertNotNull(model.getReferences());
  }

  @Test
  public void getFastModeInitial() {
    boolean fastMode = model.getFastMode();
    assertFalse(fastMode);
  }

  @Test
  public void getFastModeOn() {
    model.setFastMode(true);
    boolean fastMode = model.getFastMode();
    assertTrue(fastMode);
  }

  @Test
  public void getFastModeOff() {
    model.setFastMode(false);
    boolean fastMode = model.getFastMode();
    assertFalse(fastMode);
  }
}
