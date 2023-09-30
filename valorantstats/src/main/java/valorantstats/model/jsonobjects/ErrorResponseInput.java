package valorantstats.model.jsonobjects;

/** Represents an error response from Pandascore */
public class ErrorResponseInput {
  String error;

  public ErrorResponseInput(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }
}
