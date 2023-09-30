package valorantstats.model.apicalls.dummy;

/** Dummy Api for when the output api is offline */
public class OutputDummyApi {
  /**
   * @return string in JSON format representing data from a real api call when an image is uploaded
   *     to imgur successfully
   */
  public String postRequestImgurShortFormDummy() {
    String string =
        "{\"status\":200,\"success\":true,\"data\":{\"id\":\"8KSCiao\",\"deletehash\":\"sN8Q0Fh3WTGyZOR\",\"account_id\":null,\"account_url\":null,\"ad_type\":null,\"ad_url\":null,\"title\":null,\"description\":null,\"name\":\"\",\"type\":\"image/jpeg\",\"width\":700,\"height\":798,\"size\":65815,\"views\":0,\"section\":null,\"vote\":null,\"bandwidth\":0,\"animated\":false,\"favorite\":false,\"in_gallery\":false,\"in_most_viral\":false,\"has_sound\":false,\"is_ad\":false,\"nsfw\":null,\"link\":\"dummyLink\",\"tags\":[],\"datetime\":1653553287,\"mp4\":\"\",\"hls\":\"\"}}";
    return string;
  }
}
