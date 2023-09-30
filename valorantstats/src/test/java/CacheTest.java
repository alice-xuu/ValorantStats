import valorantstats.model.Database;
import valorantstats.model.ModelFacade;
import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.apicalls.OutputApiCalls;
import valorantstats.model.enums.CallResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CacheTest {
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
  }

  @Test
  public void cacheTeam() {
    model.cacheTeam("Acend", "results");
    verify(databaseMock, times(1)).saveTeamResult("Acend", "results");
  }

  @Test
  public void cacheMatch() {
    model.cacheMatch("123", "results");
    verify(databaseMock, times(1)).saveMatchResult("123", "results");
  }

  @Test
  public void searchCacheTeamOnlineValid() {
    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.SUCCESS, "results");
    when(databaseMock.queryTeam("Sentinels"))
        .thenReturn(new SimpleEntry<>(CallResult.SUCCESS, "results"));

    SimpleEntry<CallResult, String> result = model.searchCacheTeam("Sentinels");
    verify(databaseMock, times(1)).queryTeam("Sentinels");
    assertEquals(expected, result);
  }

  @Test
  public void searchCacheTeamOnlineNoCache() {

    when(databaseMock.queryTeam("name")).thenReturn(null);

    SimpleEntry<CallResult, String> result = model.searchCacheTeam("name");
    verify(databaseMock, times(1)).queryTeam("name");
    assertEquals(null, result);
  }

  @Test
  public void ssearchCacheMatchOnlineValid() {
    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.SUCCESS, "results");
    when(databaseMock.queryMatch("Sentinels"))
        .thenReturn(new SimpleEntry<>(CallResult.SUCCESS, "results"));

    SimpleEntry<CallResult, String> result = model.searchCacheMatch("Sentinels");
    verify(databaseMock, times(1)).queryMatch("Sentinels");
    assertEquals(expected, result);
  }

  @Test
  public void searchCacheMatchOnlineNoCache() {

    when(databaseMock.queryMatch("name")).thenReturn(null);

    SimpleEntry<CallResult, String> result = model.searchCacheMatch("name");
    verify(databaseMock, times(1)).queryMatch("name");
    assertEquals(null, result);
  }

  @Test
  public void updateCacheTeamAndMatchClear() {
    model.updateCacheTeam("Acend", "results");
    verify(databaseMock, times(1)).updateTeam("Acend", "results");
    model.updateCacheMatch("1234", "results");
    verify(databaseMock, times(1)).updateMatch("1234", "results");
    model.clearCache();
    verify(databaseMock, times(1)).clearCache();
  }

  @Test
  public void verifyCachedResultSuccess() {
    String team =
        "[{\"acronym\":null,\"current_videogame\":{\"id\":26,\"name\":\"Valorant\",\"slug\":\"valorant\"},\"id\":128472,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128472/600px_sentinels_logo.png\",\"location\":\"US\"}]";

    SimpleEntry<CallResult, String> expected =
        new SimpleEntry<CallResult, String>(CallResult.SUCCESS, team);

    SimpleEntry<CallResult, String> result =
        model.teamVerifyCachedResult(new SimpleEntry<CallResult, String>(CallResult.SUCCESS, team));
    assertEquals(expected, result);
  }

  @Test
  public void verifyCachedResultERROR() {
    String invalidTeam = "error";

    SimpleEntry<CallResult, String> expected =
        new SimpleEntry<CallResult, String>(CallResult.ERROR, invalidTeam);

    SimpleEntry<CallResult, String> result =
        model.teamVerifyCachedResult(
            new SimpleEntry<CallResult, String>(CallResult.ERROR, invalidTeam));
    assertEquals(expected, result);
  }

}
