package valorantstats.model.jsonobjects;

/** Represents a Wit.Ai Intent */
public class Intent {
  String id;
  String name;
  float confidence;

  public Intent(String id, String name, float confidence) {
    this.id = id;
    this.name = name;
    this.confidence = confidence;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public float getConfidence() {
    return confidence;
  }
}
