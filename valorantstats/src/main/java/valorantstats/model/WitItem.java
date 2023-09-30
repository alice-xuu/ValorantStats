package valorantstats.model;

import valorantstats.model.enums.CallResult;
import valorantstats.model.enums.ComponentType;
import valorantstats.model.enums.MsgIntent;
import valorantstats.model.enums.PageType;

public class WitItem {
  CallResult callResult;
  String errorMsg;
  MsgIntent intent;
  PageType pageEntity;
  ComponentType componentEntity;
  String colourEntity;
  String teamNameEntity;

  public WitItem(CallResult callResult, String errorMsg) {
    this.callResult = callResult;
    this.errorMsg = errorMsg;
  }

  public WitItem(
      CallResult callResult, MsgIntent changeColour, ComponentType componentType, String value) {
    this.callResult = callResult;
    this.intent = changeColour;
    this.componentEntity = componentType;
    this.colourEntity = value;
  }

  public WitItem(CallResult callResult, MsgIntent navigate, PageType pageType) {
    this.callResult = callResult;
    this.intent = navigate;
    this.pageEntity = pageType;
  }

  public WitItem(CallResult callResult, MsgIntent navigate, String teamName) {
    this.callResult = callResult;
    this.intent = navigate;
    this.teamNameEntity = teamName;
  }

  public WitItem(CallResult callResult, MsgIntent searchMatches) {
    this.callResult = callResult;
    this.intent = searchMatches;
  }

  public CallResult getCallResult() {
    return callResult;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public MsgIntent getIntent() {
    return intent;
  }

  public PageType getPageEntity() {
    return pageEntity;
  }

  public ComponentType getComponentEntity() {
    return componentEntity;
  }

  public String getColourEntity() {
    return colourEntity;
  }

  public String getTeamNameEntity() {
    return teamNameEntity;
  }
}
