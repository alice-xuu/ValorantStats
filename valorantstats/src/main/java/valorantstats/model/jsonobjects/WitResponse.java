package valorantstats.model.jsonobjects;

import java.util.HashMap;
import java.util.List;

/** Represents meaning of a sentence data retrieved from Wit.Ai */
public class WitResponse {

  String text;
  List<Intent> intents;
  HashMap<String, List<Entity>> entities;

  public WitResponse(String text, List<Intent> intents, HashMap<String, List<Entity>> entities) {
    this.text = text;
    this.intents = intents;
    this.entities = entities;
  }

  public String getText() {
    return text;
  }

  public List<Intent> getIntents() {
    return intents;
  }

  public HashMap<String, List<Entity>> getEntities() {
    return entities;
  }
}
