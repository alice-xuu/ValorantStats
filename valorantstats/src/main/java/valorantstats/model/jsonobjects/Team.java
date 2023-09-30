package valorantstats.model.jsonobjects;

import java.util.List;

/** Represents team data retrieved from Pandascore */
public class Team {
  private final String acronym;
  private final CurrentVideoGame cvg;
  private final Integer id;
  private final String image_url;
  private final String location;
  private final String modified_at;
  private final String name;
  private final List<Player> players;
  private final String slug;

  public Team(
      String acronym,
      CurrentVideoGame cvg,
      Integer id,
      String image_url,
      String location,
      String modified_at,
      String name,
      List<Player> players,
      String slug) {
    this.acronym = acronym;
    this.cvg = cvg;
    this.id = id;
    this.image_url = image_url;
    this.location = location;
    this.modified_at = modified_at;
    this.name = name;
    this.players = players;
    this.slug = slug;
  }

  public String getAcronym() {
    return acronym;
  }

  public CurrentVideoGame getCvg() {
    return cvg;
  }

  public Integer getId() {
    return id;
  }

  public String getImage_url() {
    return image_url;
  }

  public String getLocation() {
    return location;
  }

  public String getModified_at() {
    return modified_at;
  }

  public String getName() {
    return name;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public String getSlug() {
    return slug;
  }

  public class CurrentVideoGame {
    private final Integer id;
    private final String name;
    private final String slug;

    public CurrentVideoGame(Integer id, String name, String slug) {
      this.id = id;
      this.name = name;
      this.slug = slug;
    }

    public Integer getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public String getSlug() {
      return slug;
    }
  }
}
