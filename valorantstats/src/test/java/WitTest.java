import valorantstats.model.Database;
import valorantstats.model.ModelFacade;
import valorantstats.model.WitItem;
import valorantstats.model.apicalls.*;
import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.ComponentType;
import valorantstats.model.enums.MsgIntent;
import valorantstats.model.enums.PageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class WitTest {

  /**
   * String's comments indicate the intent of the input and the brackets () indicate the possible
   * input
   */
  // search for a team (search for Acend)
  String exampleSearchTeam =
      "{\"text\":\"search for Acend\",\"intents\":[{\"id\":\"515988193404867\",\"name\":\"searchTeam\",\"confidence\":0.9968}],\"entities\":{\"teamName:teamName\":[{\"id\":\"687441072325126\",\"name\":\"teamName\",\"role\":\"teamName\",\"start\":11,\"end\":16,\"body\":\"Acend\",\"confidence\":0.9995,\"entities\":{},\"value\":\"Acend\",\"type\":\"value\"}],\"page:page\":[{\"id\":\"331807119031926\",\"name\":\"page\",\"role\":\"page\",\"start\":0,\"end\":6,\"body\":\"search\",\"confidence\":1,\"entities\":{},\"value\":\"search\",\"type\":\"value\"}]},\"traits\":{}}";

  // search for matches (search matches)
  String exampleSearchMatch =
      "{\"text\":\"search match\",\"intents\":[{\"id\":\"1199124194173390\",\"name\":\"searchMatches\",\"confidence\":0.9927}],\"entities\":{\"page:page\":[{\"id\":\"331807119031926\",\"name\":\"page\",\"role\":\"page\",\"start\":0,\"end\":6,\"body\":\"search\",\"confidence\":0.9994,\"entities\":{},\"value\":\"search\",\"type\":\"value\"}]},\"traits\":{}}";

  // change component colour (make text green)
  String exampleChangeColour =
      "{\"text\":\"make text green\",\"intents\":[{\"id\":\"721610882614678\",\"name\":\"changeColour\",\"confidence\":0.8356}],\"entities\":{\"colour:colour\":[{\"id\":\"428371952083477\",\"name\":\"colour\",\"role\":\"colour\",\"start\":10,\"end\":15,\"body\":\"green\",\"confidence\":0.9987,\"entities\":{},\"value\":\"green\",\"type\":\"value\"}],\"component:component\":[{\"id\":\"755841718924337\",\"name\":\"component\",\"role\":\"component\",\"start\":5,\"end\":9,\"body\":\"text\",\"confidence\":1,\"entities\":{},\"value\":\"text\",\"type\":\"value\"}]},\"traits\":{}}";

  // go to a page (go to about page)
  String exampleNavigation =
      "{\"text\":\"go to about page\",\"intents\":[{\"id\":\"315551737438083\",\"name\":\"navigate\",\"confidence\":0.9962}],\"entities\":{\"page:page\":[{\"id\":\"331807119031926\",\"name\":\"page\",\"role\":\"page\",\"start\":6,\"end\":11,\"body\":\"about\",\"confidence\":0.9846,\"entities\":{},\"value\":\"about\",\"type\":\"value\"}]},\"traits\":{}}";

  // no intent found (abc)
  String exampleNoIntent =
      "{\"text\":\"abc\",\"intents\":[],\"entities\":{\"teamName:teamName\":[{\"id\":\"687441072325126\",\"name\":\"teamName\",\"role\":\"teamName\",\"start\":0,\"end\":3,\"body\":\"abc\",\"confidence\":0.9857,\"entities\":{},\"suggested\":true,\"value\":\"abc\",\"type\":\"value\"}]},\"traits\":{}}";

  // error message (no server key set)
  String exampleError = "{\"error\":\"Bad auth, check token\\/params\",\"code\":\"no-auth\"}";

  // search for a team - no team given (search for)
  String exampleSearchTeamNoTeam =
      "{\"text\":\"search for\",\"intents\":[{\"id\":\"515988193404867\",\"name\":\"searchTeam\",\"confidence\":0.9747}],\"entities\":{\"page:page\":[{\"id\":\"331807119031926\",\"name\":\"page\",\"role\":\"page\",\"start\":0,\"end\":6,\"body\":\"search\",\"confidence\":1,\"entities\":{},\"value\":\"search\",\"type\":\"value\"}]},\"traits\":{}}";

  // change colour - no component or colour given (colour)
  String exampleChangeColourNoEntity =
      "{\"text\":\"Colour\",\"intents\":[{\"id\":\"721610882614678\",\"name\":\"changeColour\",\"confidence\":0.9606}],\"entities\":{},\"traits\":{}}";

  // navigation - no page given (navigate)
  String exampleNavigationNoEntity =
      "{\"text\":\"navigate\",\"intents\":[{\"id\":\"315551737438083\",\"name\":\"navigate\",\"confidence\":0.9944}],\"entities\":{},\"traits\":{}}";

  ModelFacade model;
  Database databaseMock;
  InputApiCalls inputApiCallsMock;
  OutputApiCalls outputApiCallsMock;
  WitAiApiCalls witAiApiCallsMock;

  @BeforeEach
  public void createBackendOnline() {
    databaseMock = mock(Database.class);
    inputApiCallsMock = mock(InputApiCalls.class);
    outputApiCallsMock = mock(OutputApiCalls.class);
    witAiApiCallsMock = mock(WitAiApiCallsImpl.class);

    model = new ModelFacade(true, true, databaseMock);

    model.setInputApiCalls(inputApiCallsMock);
    model.setOutputApiCalls(outputApiCallsMock);
    model.getApiCaller().setInputApiCalls(inputApiCallsMock);
    model.setWitAiApiCalls(witAiApiCallsMock);
  }

  @Test
  public void errorGetRequestMessage() {
    ApiResponse response = new ApiResponse(400, exampleError);

    String input = "search for Acend";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem witItem = model.retrieveSentenceMeaning(input);
    assertEquals(CallResult.ERROR, witItem.getCallResult());
    assertEquals("Bad auth, check token/params", witItem.getErrorMsg());
  }

  @Test
  public void noIntentMessage() {
    ApiResponse response = new ApiResponse(200, exampleNoIntent);

    String input = "abc";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem witItem = model.retrieveSentenceMeaning(input);
    assertEquals(CallResult.ERROR, witItem.getCallResult());
    assertEquals("No intent found", witItem.getErrorMsg());
  }

  @Test
  public void searchTeamMessage() {
    ApiResponse response = new ApiResponse(200, exampleSearchTeam);

    String input = "search for Acend";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem expected = new WitItem(CallResult.SUCCESS, MsgIntent.SEARCHTEAM, "Acend");

    WitItem witItem = model.retrieveSentenceMeaning(input);

    assertEquals(expected.getCallResult(), witItem.getCallResult());
    assertEquals(expected.getIntent(), witItem.getIntent());
    assertEquals(expected.getTeamNameEntity(), witItem.getTeamNameEntity());
  }

  @Test
  public void searchTeamMessageNoTeam() {
    ApiResponse response = new ApiResponse(200, exampleSearchTeamNoTeam);

    String input = "search for";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem expected = new WitItem(CallResult.ERROR, "No team name found");

    WitItem witItem = model.retrieveSentenceMeaning(input);

    assertEquals(expected.getCallResult(), witItem.getCallResult());
    assertEquals(expected.getErrorMsg(), witItem.getErrorMsg());
  }

  @Test
  public void searchMatchMessage() {
    ApiResponse response = new ApiResponse(200, exampleSearchMatch);

    String input = "search matches";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem expected = new WitItem(CallResult.SUCCESS, MsgIntent.SEARCHMATCHES);

    WitItem witItem = model.retrieveSentenceMeaning(input);

    assertEquals(expected.getCallResult(), witItem.getCallResult());
    assertEquals(expected.getIntent(), witItem.getIntent());
  }

  @Test
  public void changeColourMessage() {
    ApiResponse response = new ApiResponse(200, exampleChangeColour);

    String input = "make text green";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem expected =
        new WitItem(CallResult.SUCCESS, MsgIntent.CHANGECOLOUR, ComponentType.TEXT, "green");

    WitItem witItem = model.retrieveSentenceMeaning(input);

    assertEquals(expected.getCallResult(), witItem.getCallResult());
    assertEquals(expected.getIntent(), witItem.getIntent());
    assertEquals(expected.getColourEntity(), witItem.getColourEntity());
    assertEquals(expected.getComponentEntity(), witItem.getComponentEntity());
  }

  @Test
  public void changeColourMessageNoEntity() {
    ApiResponse response = new ApiResponse(200, exampleChangeColourNoEntity);

    String input = "colour";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem expected = new WitItem(CallResult.ERROR, "No component found");

    WitItem witItem = model.retrieveSentenceMeaning(input);

    assertEquals(expected.getCallResult(), witItem.getCallResult());
    assertEquals(expected.getErrorMsg(), witItem.getErrorMsg());
  }

  @Test
  public void navigationMessage() {
    ApiResponse response = new ApiResponse(200, exampleNavigation);

    String input = "go to about page";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem expected = new WitItem(CallResult.SUCCESS, MsgIntent.NAVIGATE, PageType.ABOUT);

    WitItem witItem = model.retrieveSentenceMeaning(input);

    assertEquals(expected.getCallResult(), witItem.getCallResult());
    assertEquals(expected.getIntent(), witItem.getIntent());
    assertEquals(expected.getPageEntity(), witItem.getPageEntity());
  }

  @Test
  public void navigationMessageNoEntity() {
    ApiResponse response = new ApiResponse(200, exampleNavigationNoEntity);

    String input = "navigate";
    doReturn(response).when(witAiApiCallsMock).getRequestMessage(input);

    WitItem expected = new WitItem(CallResult.ERROR, "No page found");

    WitItem witItem = model.retrieveSentenceMeaning(input);

    assertEquals(expected.getCallResult(), witItem.getCallResult());
    assertEquals(expected.getErrorMsg(), witItem.getErrorMsg());
  }
}
