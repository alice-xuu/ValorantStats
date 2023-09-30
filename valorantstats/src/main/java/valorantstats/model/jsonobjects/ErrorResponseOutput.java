package valorantstats.model.jsonobjects;

/** Represents an error response from Imgur */
public class ErrorResponseOutput {
  Data data;

  public ErrorResponseOutput(Data data) {
    this.data = data;
  }

  public Data getData() {
    return data;
  }

  public class Data {
    String error;

    public Data(String error) {
      this.error = error;
    }

    public String getError() {
      return error;
    }
  }
}
