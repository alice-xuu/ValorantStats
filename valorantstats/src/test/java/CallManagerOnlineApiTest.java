import valorantstats.model.Database;
import valorantstats.model.ModelFacade;
import valorantstats.model.apicalls.ApiResponse;
import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.apicalls.OutputApiCalls;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CallManagerOnlineApiTest {

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

  // real team
  @Test
  public void searchTeamValid() {

    String body = "[{\"name\":\"Sentinels\"}]";
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("Sentinels");

    SimpleEntry<CallResult, String> result = model.searchTeam("Sentinels", true);
    assertEquals(CallResult.SUCCESS, result.getKey());
    assertEquals(body, result.getValue());
  }

  // invalid team, string similar to real team
  @Test
  public void searchTeamInvalid() {

    String body = "[]";
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("Sentine");

    SimpleEntry<CallResult, String> result = model.searchTeam("Sentine", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("No team found: Sentine", result.getValue());
  }

  // no team found
  @Test
  public void searchTeamEmpty() {

    String body = "[]";
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("non existent team");

    SimpleEntry<CallResult, String> result = model.searchTeam("non existent team", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("No team found: non existent team", result.getValue());
  }

  @Test
  public void searchTeamNoInput() {

    SimpleEntry<CallResult, String> result = model.searchTeam("", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("Invalid input", result.getValue());
  }

  @Test
  public void searchTeamBadRequest() {

    String body = "{\"error\": \"error message\"}";
    ApiResponse response = new ApiResponse(400, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("error message", result.getValue());
  }

  @Test
  public void searchTeamUnauthorised() {

    String body = "{\"error\": \"error message\"}";
    ApiResponse response = new ApiResponse(401, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("error message", result.getValue());
  }

  @Test
  public void searchTeamForbidden() {

    String body = "{\"error\": \"error message\"}";
    ApiResponse response = new ApiResponse(403, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("error message", result.getValue());
  }

  @Test
  public void searchTeamNotFound() {

    String body = "{\"error\": \"error message\"}";
    ApiResponse response = new ApiResponse(404, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("error message", result.getValue());
  }

  @Test
  public void searchTeamUnprocessableEntity() {

    String body = "{\"error\": \"error message\"}";
    ApiResponse response = new ApiResponse(422, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("error message", result.getValue());
  }

  @Test
  public void searchTeamUpcomingMatchesValid() {

    String body = "[{\"league_id\":4531}]";
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeamUpcomingMatches("4531");

    SimpleEntry<CallResult, String> result = model.searchTeamUpcomingMatches("4531", true);
    assertEquals(CallResult.SUCCESS, result.getKey());
    assertEquals("[{\"league_id\":4531}]", result.getValue());
    verify(inputApiCallsMock, times(1)).getRequestTeamUpcomingMatches("4531");
  }

  @Test
  public void searchTeamUpcomingMatchesNoMatches() throws IOException, InterruptedException {

    String body = "[]";
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeamUpcomingMatches("4531");

    SimpleEntry<CallResult, String> result = model.searchTeamUpcomingMatches("4531", true);
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("No matches found", result.getValue());
  }

  @Test
  public void uploadImage() throws IOException, InterruptedException {

    String body =
        "{\"status\":200,\"success\":true,\"data\":{\"id\":\"8KSCiao\",\"deletehash\":\"sN8Q0Fh3WTGyZOR\",\"account_id\":null,\"account_url\":null,\"ad_type\":null,\"ad_url\":null,\"title\":null,\"description\":null,\"name\":\"\",\"type\":\"image/jpeg\",\"width\":700,\"height\":798,\"size\":65815,\"views\":0,\"section\":null,\"vote\":null,\"bandwidth\":0,\"animated\":false,\"favorite\":false,\"in_gallery\":false,\"in_most_viral\":false,\"has_sound\":false,\"is_ad\":false,\"nsfw\":null,\"link\":\"https://i.imgur.com/8KSCiao.jpeg\",\"tags\":[],\"datetime\":1653553287,\"mp4\":\"\",\"hls\":\"\"}}";

    String link = "https://i.imgur.com/8KSCiao.jpeg";
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(outputApiCallsMock).postRequestImgurShortForm("data");

    SimpleEntry<CallResult, String> result = model.uploadImage("data");
    assertEquals(CallResult.SUCCESS, result.getKey());
    assertEquals(link, result.getValue());
    verify(outputApiCallsMock, times(1)).postRequestImgurShortForm("data");
  }

  @Test
  public void uploadImageBadRequest() throws IOException, InterruptedException {

    String body =
        "{\"status\":400,\"success\":false,\"data\":{\"error\":\"Bad Request\",\"request\":\"/3/upload\",\"method\":\"POST\"}}";

    ApiResponse response = new ApiResponse(400, body);
    doReturn(response).when(outputApiCallsMock).postRequestImgurShortForm("bad data");

    SimpleEntry<CallResult, String> result = model.uploadImage("bad data");
    assertEquals(CallResult.ERROR, result.getKey());
    assertEquals("Bad Request", result.getValue());
  }

  @Test
  public void createQR() {

    model.createQR("data");
    model.bufferedImageToB64(new BufferedImage(1, 1, TYPE_3BYTE_BGR));
  }

  @Test
  public void validateUserInput() {
    assertFalse(model.validateUserInputName(""));
    assertTrue(model.validateUserInputName("yes"));
    assertTrue(model.validateUserInputName("two words"));
  }

  @Test
  public void gsonParsingTeams() {
    Team[] teams =
        model.stringToTeamArray(
            "[{\"acronym\":null,\"current_videogame\":{\"id\":26,\"name\":\"Valorant\",\"slug\":\"valorant\"},\"id\":128472,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128472/600px_sentinels_logo.png\",\"location\":\"US\"}]");
    assertEquals(null, teams[0].getAcronym());
    assertEquals(128472, teams[0].getId());
    assertEquals("US", teams[0].getLocation());
  }

  @Test
  public void gsonParsingTeamsNoTeams() {
    Team[] teams = model.stringToTeamArray("[]");
    assertEquals(0, teams.length);
  }

  @Test
  public void gsonParsingTeamOne() {
    String string =
        "[{\"acronym\":null,\"current_videogame\":{\"id\":26,\"name\":\"Valorant\",\"slug\":\"valorant\"},\"id\":128472,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128472/600px_sentinels_logo.png\",\"location\":\"US\",\"modified_at\":\"2022-04-29T17:31:03Z\",\"name\":\"Sentinels\",\"players\":[{\"age\":22,\"birth_year\":2000,\"birthday\":\"2000-03-18\",\"first_name\":\"Jay\",\"hometown\":\"Shoreline, WA\",\"id\":8540,\"image_url\":\"https://cdn.pandascore.co/images/player/image/8540/shock_sinatraa.png\",\"last_name\":\"Won\",\"name\":\"sinatraa\",\"nationality\":\"US\",\"role\":\"Damage\",\"slug\":\"sinatraa\"},{\"age\":23,\"birth_year\":1998,\"birthday\":\"1998-09-02\",\"first_name\":\"Hunter\",\"hometown\":\"United States\",\"id\":17716,\"image_url\":\"https://cdn.pandascore.co/images/player/image/17716/complexity_sic_k_2019.png\",\"last_name\":\"Mims\",\"name\":\"SicK\",\"nationality\":\"US\",\"role\":null,\"slug\":\"sick\"},{\"age\":23,\"birth_year\":1998,\"birthday\":\"1998-10-02\",\"first_name\":\"Jared\",\"hometown\":null,\"id\":32528,\"image_url\":\"https://cdn.pandascore.co/images/player/image/32528/600px_zombs_at_apex_legends_preseason_invitational.png\",\"last_name\":\"Gitlin\",\"name\":\"zombs\",\"nationality\":\"US\",\"role\":null,\"slug\":\"zombs-jared-gitlin\"}],\"slug\":\"sentinels\"}]\n";

    Team team = model.stringToTeamOne(string);
    assertEquals(null, team.getAcronym());
    assertEquals(128472, team.getId());
    assertEquals("US", team.getLocation());
  }

  @Test
  public void gsonParsingMatches() {
    String string =
        "[{\"league_id\":4531,\"streams\":{\"english\":{\"embed_url\":null,\"raw_url\":null},\"official\":{\"embed_url\":null,\"raw_url\":null},\"russian\":{\"embed_url\":null,\"raw_url\":null}},\"modified_at\":\"2022-05-01T08:03:26Z\",\"draw\":false,\"results\":[{\"score\":0,\"team_id\":128472},{\"score\":0,\"team_id\":128474}],\"game_advantage\":null,\"original_scheduled_at\":\"2022-05-01T20:00:00Z\",\"detailed_stats\":true,\"end_at\":null,\"streams_list\":[],\"id\":630791,\"tournament_id\":8027,\"tournament\":{\"begin_at\":\"2022-04-29T22:00:00Z\",\"end_at\":\"2022-05-02T05:00:00Z\",\"id\":8027,\"league_id\":4531,\"live_supported\":false,\"modified_at\":\"2022-04-30T20:46:51Z\",\"name\":\"Open Qualifier 1\",\"prizepool\":null,\"serie_id\":4614,\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1-north-america-stage-2-challengers-2022-open-qualifier-1\",\"tier\":\"d\",\"winner_id\":null,\"winner_type\":\"Team\"},\"scheduled_at\":\"2022-05-01T20:00:00Z\",\"begin_at\":\"2022-05-01T20:00:00Z\",\"winner_id\":null,\"forfeit\":false,\"number_of_games\":3,\"videogame\":{\"id\":26,\"name\":\"Valorant\",\"slug\":\"valorant\"},\"serie\":{\"begin_at\":\"2022-04-29T22:00:00Z\",\"description\":null,\"end_at\":null,\"full_name\":\"North America Stage 2: Challengers 2022\",\"id\":4614,\"league_id\":4531,\"modified_at\":\"2022-04-29T13:20:05Z\",\"name\":\"North America Stage 2: Challengers\",\"season\":null,\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1-north-america-stage-2-challengers-2022\",\"tier\":\"d\",\"winner_id\":null,\"winner_type\":null,\"year\":2022},\"live\":{\"opens_at\":null,\"supported\":false,\"url\":null},\"games\":[{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7908,\"length\":null,\"match_id\":630791,\"position\":1,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"},{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7909,\"length\":null,\"match_id\":630791,\"position\":2,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"},{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7910,\"length\":null,\"match_id\":630791,\"position\":3,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"}],\"slug\":\"2022-05-01-9e018901-4932-4be5-8645-ca88cbd6e5f2\",\"status\":\"not_started\",\"live_embed_url\":null,\"match_type\":\"best_of\",\"winner\":null,\"serie_id\":4614,\"rescheduled\":false,\"league\":{\"id\":4531,\"image_url\":\"https://cdn.pandascore.co/images/league/image/4531/600px-VALORANT_Challengers_logo.png\",\"modified_at\":\"2021-02-02T09:21:06Z\",\"name\":\"VALORANT Champions Tour\",\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1\",\"url\":null},\"opponents\":[{\"opponent\":{\"acronym\":null,\"id\":128472,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128472/600px_sentinels_logo.png\",\"location\":\"US\",\"modified_at\":\"2022-04-29T17:31:03Z\",\"name\":\"Sentinels\",\"slug\":\"sentinels\"},\"type\":\"Team\"},{\"opponent\":{\"acronym\":null,\"id\":128474,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128474/600px_luminosity_gaming_2018_infoboximage.png\",\"location\":\"CA\",\"modified_at\":\"2022-04-29T23:30:17Z\",\"name\":\"Luminosity Gaming\",\"slug\":\"luminosity-gaming-valorant\"},\"type\":\"Team\"}],\"official_stream_url\":null,\"name\":\"Lower bracket round 5 match 2: Sentinels vs Luminosity Gaming\",\"videogame_version\":null}]\n";
    Match[] matches = model.stringToMatchArray(string);
    assertEquals(4531, matches[0].getLeague_id());
    assertEquals(3, matches[0].getNumber_of_games());
    assertEquals(
        "Lower bracket round 5 match 2: Sentinels vs Luminosity Gaming", matches[0].getName());
  }

  @Test
  public void gsonParsingTeamsNoMatches() {
    Match[] matches = model.stringToMatchArray("[]");
    assertEquals(0, matches.length);
  }

  // Test team call handler routes for cache and api (mock) calls

  @Test
  public void teamFalseCacheOverrideCacheHit() {

    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.CACHEHIT, "result");
    doReturn(expected).when(databaseMock).queryTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", false);
    assertEquals(expected, result);
  }

  // false cache override -> no cache result -> api call -> store new api result
  @Test
  public void teamFalseCacheOverrideCacheMiss() {
    String body = "[{\"name\":\"Sentinels\"}]";
    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.SUCCESS, body);
    doReturn(null).when(databaseMock).queryTeam("team");
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", false);
    assertEquals(expected, result);
    verify(databaseMock, times(1)).saveTeamResult("team", body);
  }

  @Test
  public void teamTrueCacheOverrideCacheMiss() {
    String body = "[{\"name\":\"Sentinels\"}]";
    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.SUCCESS, body);
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeam("team");

    SimpleEntry<CallResult, String> result = model.searchTeam("team", true);
    assertEquals(expected, result);
    verify(databaseMock, times(1)).updateTeam("team", body);
    verify(databaseMock, times(0)).saveTeamResult("team", body);
  }

  // Test match call handler routes

  @Test
  public void matchFalseCacheOverrideCacheHit() {

    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.CACHEHIT, "result");
    doReturn(expected).when(databaseMock).queryMatch("id");

    SimpleEntry<CallResult, String> result = model.searchTeamUpcomingMatches("id", false);
    assertEquals(expected, result);
  }

  // false cache override -> no cache result -> api call -> store new api result
  @Test
  public void matchFalseCacheOverrideCacheMiss() {
    String body =
        "[{\"league_id\":4531,\"streams\":{\"english\":{\"embed_url\":null,\"raw_url\":null},\"official\":{\"embed_url\":null,\"raw_url\":null},\"russian\":{\"embed_url\":null,\"raw_url\":null}},\"modified_at\":\"2022-05-01T08:03:26Z\",\"draw\":false,\"results\":[{\"score\":0,\"team_id\":128472},{\"score\":0,\"team_id\":128474}],\"game_advantage\":null,\"original_scheduled_at\":\"2022-05-01T20:00:00Z\",\"detailed_stats\":true,\"end_at\":null,\"streams_list\":[],\"id\":630791,\"tournament_id\":8027,\"tournament\":{\"begin_at\":\"2022-04-29T22:00:00Z\",\"end_at\":\"2022-05-02T05:00:00Z\",\"id\":8027,\"league_id\":4531,\"live_supported\":false,\"modified_at\":\"2022-04-30T20:46:51Z\",\"name\":\"Open Qualifier 1\",\"prizepool\":null,\"serie_id\":4614,\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1-north-america-stage-2-challengers-2022-open-qualifier-1\",\"tier\":\"d\",\"winner_id\":null,\"winner_type\":\"Team\"},\"scheduled_at\":\"2022-05-01T20:00:00Z\",\"begin_at\":\"2022-05-01T20:00:00Z\",\"winner_id\":null,\"forfeit\":false,\"number_of_games\":3,\"videogame\":{\"id\":26,\"name\":\"Valorant\",\"slug\":\"valorant\"},\"serie\":{\"begin_at\":\"2022-04-29T22:00:00Z\",\"description\":null,\"end_at\":null,\"full_name\":\"North America Stage 2: Challengers 2022\",\"id\":4614,\"league_id\":4531,\"modified_at\":\"2022-04-29T13:20:05Z\",\"name\":\"North America Stage 2: Challengers\",\"season\":null,\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1-north-america-stage-2-challengers-2022\",\"tier\":\"d\",\"winner_id\":null,\"winner_type\":null,\"year\":2022},\"live\":{\"opens_at\":null,\"supported\":false,\"url\":null},\"games\":[{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7908,\"length\":null,\"match_id\":630791,\"position\":1,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"},{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7909,\"length\":null,\"match_id\":630791,\"position\":2,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"},{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7910,\"length\":null,\"match_id\":630791,\"position\":3,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"}],\"slug\":\"2022-05-01-9e018901-4932-4be5-8645-ca88cbd6e5f2\",\"status\":\"not_started\",\"live_embed_url\":null,\"match_type\":\"best_of\",\"winner\":null,\"serie_id\":4614,\"rescheduled\":false,\"league\":{\"id\":4531,\"image_url\":\"https://cdn.pandascore.co/images/league/image/4531/600px-VALORANT_Challengers_logo.png\",\"modified_at\":\"2021-02-02T09:21:06Z\",\"name\":\"VALORANT Champions Tour\",\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1\",\"url\":null},\"opponents\":[{\"opponent\":{\"acronym\":null,\"id\":128472,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128472/600px_sentinels_logo.png\",\"location\":\"US\",\"modified_at\":\"2022-04-29T17:31:03Z\",\"name\":\"Sentinels\",\"slug\":\"sentinels\"},\"type\":\"Team\"},{\"opponent\":{\"acronym\":null,\"id\":128474,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128474/600px_luminosity_gaming_2018_infoboximage.png\",\"location\":\"CA\",\"modified_at\":\"2022-04-29T23:30:17Z\",\"name\":\"Luminosity Gaming\",\"slug\":\"luminosity-gaming-valorant\"},\"type\":\"Team\"}],\"official_stream_url\":null,\"name\":\"Lower bracket round 5 match 2: Sentinels vs Luminosity Gaming\",\"videogame_version\":null}]\n";
    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.SUCCESS, body);
    doReturn(null).when(databaseMock).queryMatch("id");
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeamUpcomingMatches("id");

    SimpleEntry<CallResult, String> result = model.searchTeamUpcomingMatches("id", false);
    assertEquals(expected, result);
    verify(databaseMock, times(1)).saveMatchResult("id", body);
  }

  @Test
  public void matchTrueCacheOverrideCacheMiss() {
    String body =
        "[{\"league_id\":4531,\"streams\":{\"english\":{\"embed_url\":null,\"raw_url\":null},\"official\":{\"embed_url\":null,\"raw_url\":null},\"russian\":{\"embed_url\":null,\"raw_url\":null}},\"modified_at\":\"2022-05-01T08:03:26Z\",\"draw\":false,\"results\":[{\"score\":0,\"team_id\":128472},{\"score\":0,\"team_id\":128474}],\"game_advantage\":null,\"original_scheduled_at\":\"2022-05-01T20:00:00Z\",\"detailed_stats\":true,\"end_at\":null,\"streams_list\":[],\"id\":630791,\"tournament_id\":8027,\"tournament\":{\"begin_at\":\"2022-04-29T22:00:00Z\",\"end_at\":\"2022-05-02T05:00:00Z\",\"id\":8027,\"league_id\":4531,\"live_supported\":false,\"modified_at\":\"2022-04-30T20:46:51Z\",\"name\":\"Open Qualifier 1\",\"prizepool\":null,\"serie_id\":4614,\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1-north-america-stage-2-challengers-2022-open-qualifier-1\",\"tier\":\"d\",\"winner_id\":null,\"winner_type\":\"Team\"},\"scheduled_at\":\"2022-05-01T20:00:00Z\",\"begin_at\":\"2022-05-01T20:00:00Z\",\"winner_id\":null,\"forfeit\":false,\"number_of_games\":3,\"videogame\":{\"id\":26,\"name\":\"Valorant\",\"slug\":\"valorant\"},\"serie\":{\"begin_at\":\"2022-04-29T22:00:00Z\",\"description\":null,\"end_at\":null,\"full_name\":\"North America Stage 2: Challengers 2022\",\"id\":4614,\"league_id\":4531,\"modified_at\":\"2022-04-29T13:20:05Z\",\"name\":\"North America Stage 2: Challengers\",\"season\":null,\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1-north-america-stage-2-challengers-2022\",\"tier\":\"d\",\"winner_id\":null,\"winner_type\":null,\"year\":2022},\"live\":{\"opens_at\":null,\"supported\":false,\"url\":null},\"games\":[{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7908,\"length\":null,\"match_id\":630791,\"position\":1,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"},{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7909,\"length\":null,\"match_id\":630791,\"position\":2,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"},{\"begin_at\":null,\"complete\":false,\"detailed_stats\":true,\"end_at\":null,\"finished\":false,\"forfeit\":false,\"id\":7910,\"length\":null,\"match_id\":630791,\"position\":3,\"status\":\"not_started\",\"video_url\":null,\"winner\":{\"id\":null,\"type\":\"Team\"},\"winner_type\":\"Team\"}],\"slug\":\"2022-05-01-9e018901-4932-4be5-8645-ca88cbd6e5f2\",\"status\":\"not_started\",\"live_embed_url\":null,\"match_type\":\"best_of\",\"winner\":null,\"serie_id\":4614,\"rescheduled\":false,\"league\":{\"id\":4531,\"image_url\":\"https://cdn.pandascore.co/images/league/image/4531/600px-VALORANT_Challengers_logo.png\",\"modified_at\":\"2021-02-02T09:21:06Z\",\"name\":\"VALORANT Champions Tour\",\"slug\":\"valorant-vct-2021-north-america-stage-1-challengers-1\",\"url\":null},\"opponents\":[{\"opponent\":{\"acronym\":null,\"id\":128472,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128472/600px_sentinels_logo.png\",\"location\":\"US\",\"modified_at\":\"2022-04-29T17:31:03Z\",\"name\":\"Sentinels\",\"slug\":\"sentinels\"},\"type\":\"Team\"},{\"opponent\":{\"acronym\":null,\"id\":128474,\"image_url\":\"https://cdn.pandascore.co/images/team/image/128474/600px_luminosity_gaming_2018_infoboximage.png\",\"location\":\"CA\",\"modified_at\":\"2022-04-29T23:30:17Z\",\"name\":\"Luminosity Gaming\",\"slug\":\"luminosity-gaming-valorant\"},\"type\":\"Team\"}],\"official_stream_url\":null,\"name\":\"Lower bracket round 5 match 2: Sentinels vs Luminosity Gaming\",\"videogame_version\":null}]\n";
    SimpleEntry<CallResult, String> expected = new SimpleEntry<>(CallResult.SUCCESS, body);
    ApiResponse response = new ApiResponse(200, body);
    doReturn(response).when(inputApiCallsMock).getRequestTeamUpcomingMatches("id");

    SimpleEntry<CallResult, String> result = model.searchTeamUpcomingMatches("id", true);
    assertEquals(expected, result);
    verify(databaseMock, times(1)).updateMatch("id", body);
    verify(databaseMock, times(0)).saveMatchResult("id", body);
  }
}
