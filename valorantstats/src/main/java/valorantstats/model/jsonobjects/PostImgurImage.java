package valorantstats.model.jsonobjects;

/** Form data for image upload to imgur */
public class PostImgurImage {
  String image;
  String type;

  /**
   * @param image A binary file, base64 data, or a URL for an image. (up to 10MB)
   * @param type The type of the file that's being sent; file, base64 or URL
   */
  public PostImgurImage(String image, String type) {
    this.image = image;
    this.type = type;
  }
}
