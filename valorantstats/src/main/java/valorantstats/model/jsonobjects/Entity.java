package valorantstats.model.jsonobjects;

/** Represents a Wit.Ai Entity */
public class Entity {
  String id;
  String name;
  String role;
  int start;
  int end;
  String body;
  float confidence;
  String value;

  public Entity(
      String id,
      String name,
      String role,
      int start,
      int end,
      String body,
      float confidence,
      String value) {
    this.id = id;
    this.name = name;
    this.role = role;
    this.start = start;
    this.end = end;
    this.body = body;
    this.confidence = confidence;
    this.value = value;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRole() {
    return role;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public String getBody() {
    return body;
  }

  public float getConfidence() {
    return confidence;
  }

  public String getValue() {
    return value;
  }
}
