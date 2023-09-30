package valorantstats.model.jsonobjects;

/** Represents player data retrieved from Pandascore */
public class Player {
  private Integer age;
  private String birthday;
  private String first_name;
  private Integer id;
  private String image_url;
  private String last_name;
  private String name;
  private String nationality;
  private String role;
  private String slug;

  public Player(
      Integer age,
      String birthday,
      String first_name,
      Integer id,
      String image_url,
      String last_name,
      String name,
      String nationality,
      String role,
      String slug) {
    this.age = age;
    this.birthday = birthday;
    this.first_name = first_name;
    this.id = id;
    this.image_url = image_url;
    this.last_name = last_name;
    this.name = name;
    this.nationality = nationality;
    this.role = role;
    this.slug = slug;
  }

  public Integer getAge() {
    return age;
  }

  public String getBirthday() {
    return birthday;
  }

  public String getFirst_name() {
    return first_name;
  }

  public int getId() {
    return id;
  }

  public String getImage_url() {
    return image_url;
  }

  public String getLast_name() {
    return last_name;
  }

  public String getName() {
    return name;
  }

  public String getNationality() {
    return nationality;
  }

  public String getRole() {
    return role;
  }

  public String getSlug() {
    return slug;
  }
}
