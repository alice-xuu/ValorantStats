package valorantstats.model.jsonobjects;

/** Represents an error response from Wit.Ai */
public class ErrorResponseWit {
  String error;
  String code;

  public ErrorResponseWit(String error, String code) {
    this.error = error;
    this.code = code;
  }

  public String getError() {
    return error;
  }

  public String getCode() {
    return code;
  }
}
