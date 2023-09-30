package valorantstats.model.jsonobjects;

/** Represents data retrieved from Imgur */
public class ImgurData {
  int status;
  boolean success;
  Data data;

  public ImgurData(int status, boolean success, Data data) {
    this.status = status;
    this.success = success;
    this.data = data;
  }

  public int getStatus() {
    return status;
  }

  public boolean isSuccess() {
    return success;
  }

  public Data getData() {
    return data;
  }

  public class Data {
    String link;

    public Data(String link) {
      this.link = link;
    }

    public String getLink() {
      return link;
    }
  }
}
