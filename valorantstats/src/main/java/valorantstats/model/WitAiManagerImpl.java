package valorantstats.model;

import com.google.gson.Gson;
import valorantstats.model.apicalls.ApiResponse;
import valorantstats.model.apicalls.WitAiApiCalls;
import valorantstats.model.apicalls.WitAiApiCallsImpl;
import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.ComponentType;
import valorantstats.model.enums.MsgIntent;
import valorantstats.model.enums.PageType;
import valorantstats.model.jsonobjects.Entity;
import valorantstats.model.jsonobjects.ErrorResponseWit;
import valorantstats.model.jsonobjects.Intent;
import valorantstats.model.jsonobjects.WitResponse;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.List;

public class WitAiManagerImpl implements WitAiManager {

  WitAiApiCalls witAiApiCalls;

  public WitAiManagerImpl() {
    this.witAiApiCalls = new WitAiApiCallsImpl();
  }

  @Override
  public WitItem retrieveSentenceMeaning(String input) {
    ApiResponse apiResponse = witAiApiCalls.getRequestMessage(input);
    SimpleEntry<CallResult, String> result = validateResponseWitApi(apiResponse);
    if (result.getKey() == CallResult.ERROR) {
      return new WitItem(CallResult.ERROR, result.getValue());
    }

    WitResponse witResponse = stringToWitData(result.getValue());

    List<Intent> intentList = witResponse.getIntents();
    if (intentList.size() < 1) {
      return new WitItem(CallResult.ERROR, "No intent found");
    }
    Intent intent = witResponse.getIntents().get(0);
    HashMap<String, List<Entity>> entityListMap = witResponse.getEntities();

    WitItem witItem;
    if (intent.getName().equals("changeColour")) {
      // return intent and entities: component and colour
      List<Entity> entityListComponent = entityListMap.get("component:component");
      if (entityListComponent == null || entityListComponent.size() == 0) {
        witItem = new WitItem(CallResult.ERROR, "No component found");
        return witItem;
      }
      List<Entity> entityListColour = entityListMap.get("colour:colour");
      if (entityListColour == null || entityListColour.size() == 0) {
        witItem = new WitItem(CallResult.ERROR, "No colour found");
        return witItem;
      }

      String componentString = entityListComponent.get(0).getValue();
      ComponentType componentType;
        switch (componentString) {
            case "background" -> componentType = ComponentType.BACKGROUND;
            case "text" -> componentType = ComponentType.TEXT;
            case "button" -> componentType = ComponentType.BUTTON;
            case "highlight" -> componentType = ComponentType.HIGHLIGHT;
            default -> {
                witItem = new WitItem(CallResult.ERROR, "No valid component found");
                return witItem;
            }
        }
      witItem = new WitItem(CallResult.SUCCESS, MsgIntent.CHANGECOLOUR, componentType, entityListColour.get(0).getValue());
      return witItem;
    }

    if (intent.getName().equals("navigate")) {
      // return intent and entity: page
      List<Entity> entityListPage = entityListMap.get("page:page");
      if (entityListPage == null || entityListPage.size() == 0) {
        witItem = new WitItem(CallResult.ERROR, "No page found");
        return witItem;
      }

        String pageString = entityListPage.get(0).getValue();
        PageType pageType;
        switch (pageString) {
            case "search" -> pageType = PageType.SEARCH;
            case "personalise" -> pageType = PageType.PERSONALISE;
            case "about" -> pageType = PageType.ABOUT;
            case "home" -> pageType = PageType.HOME;
            default -> {
                witItem = new WitItem(CallResult.ERROR, "No valid component found");
                return witItem;
            }
        }

      witItem = new WitItem(CallResult.SUCCESS, MsgIntent.NAVIGATE, pageType);
      return witItem;
    }

    if (intent.getName().equals("searchTeam")) {
      // return intent and entity: teamName
      List<Entity> entityListTeamName = entityListMap.get("teamName:teamName");

      if (entityListTeamName == null || entityListTeamName.size() == 0) {
        witItem = new WitItem(CallResult.ERROR, "No team name found");
        return witItem;
      }


      witItem = new WitItem(CallResult.SUCCESS, MsgIntent.SEARCHTEAM, entityListTeamName.get(0).getValue());
      return witItem;
    }
    if (intent.getName().equals("searchMatches")) {
      // return intent
      witItem = new WitItem(CallResult.SUCCESS, MsgIntent.SEARCHMATCHES);
      return witItem;
    }
    if (intent.getName().equals("uploadImage")) {
      witItem = new WitItem(CallResult.SUCCESS, MsgIntent.UPLOADIMAGE);
      return witItem;
    }

    return new WitItem(CallResult.ERROR, "No valid intent found");
  }

  @Override
  public WitResponse stringToWitData(String string) {
    return new Gson().fromJson(string, WitResponse.class);
  }

  @Override
  public SimpleEntry<CallResult, String> validateResponseWitApi(ApiResponse response) {
    SimpleEntry<CallResult, String> result;
    int statusCode = response.getStatusCode();
    String body = response.getBody();

    if (statusCode == 200) {
      result = new SimpleEntry<>(CallResult.SUCCESS, body);
    } else {
      ErrorResponseWit ero = new Gson().fromJson(body, ErrorResponseWit.class);
      String msg = ero.getError();
      result = new SimpleEntry<>(CallResult.ERROR, msg);
    }

    return result;
  }

  @Override
  public void setWitAiApiCalls(WitAiApiCalls witAiApiCalls) {
    this.witAiApiCalls = witAiApiCalls;
  }
}
