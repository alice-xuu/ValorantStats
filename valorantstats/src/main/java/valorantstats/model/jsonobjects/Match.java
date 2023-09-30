package valorantstats.model.jsonobjects;

import java.util.List;

/** Represents match data retrieved from Pandascore */
public class Match {

  String begin_at;
  boolean detailed_stats;
  boolean draw;
  boolean end_at;
  boolean forfeit;
  Integer game_advantage;
  List<Game> games;
  Integer id;
  League league;
  Integer league_id;
  Live live;
  String match_type;
  String modified_at;
  String name;
  Integer number_of_games;
  List<Opponent> opponents;
  String original_scheduled_at;
  boolean rescheduled;
  List<ResultObject> results;
  String scheduled_at;
  Serie serie;
  Integer serie_id;
  String slug;
  String status;
  List<Stream> streams_list;
  Integer tournament_id;
  Object videogame;
  VideogameVersion videogame_version;
  TeamObject winner; // 2 options
  Integer winner_id;

  public Match(
      String begin_at,
      boolean detailed_stats,
      boolean draw,
      boolean end_at,
      boolean forfeit,
      Integer game_advantage,
      List<Game> games,
      Integer id,
      League league,
      Integer league_id,
      Live live,
      String match_type,
      String modified_at,
      String name,
      Integer number_of_games,
      List<Opponent> opponents,
      String original_scheduled_at,
      boolean rescheduled,
      List<ResultObject> results,
      String scheduled_at,
      Serie serie,
      Integer serie_id,
      String slug,
      String status,
      List<Stream> streams_list,
      Integer tournament_id,
      Object videogame,
      VideogameVersion videogame_version,
      TeamObject winner,
      Integer winner_id) {
    this.begin_at = begin_at;
    this.detailed_stats = detailed_stats;
    this.draw = draw;
    this.end_at = end_at;
    this.forfeit = forfeit;
    this.game_advantage = game_advantage;
    this.games = games;
    this.id = id;
    this.league = league;
    this.league_id = league_id;
    this.live = live;
    this.match_type = match_type;
    this.modified_at = modified_at;
    this.name = name;
    this.number_of_games = number_of_games;
    this.opponents = opponents;
    this.original_scheduled_at = original_scheduled_at;
    this.rescheduled = rescheduled;
    this.results = results;
    this.scheduled_at = scheduled_at;
    this.serie = serie;
    this.serie_id = serie_id;
    this.slug = slug;
    this.status = status;
    this.streams_list = streams_list;
    this.tournament_id = tournament_id;
    this.videogame = videogame;
    this.videogame_version = videogame_version;
    this.winner = winner;
    this.winner_id = winner_id;
  }

  public String getBegin_at() {
    return begin_at;
  }

  public boolean isDetailed_stats() {
    return detailed_stats;
  }

  public boolean isDraw() {
    return draw;
  }

  public boolean isEnd_at() {
    return end_at;
  }

  public boolean isForfeit() {
    return forfeit;
  }

  public Integer getGame_advantage() {
    return game_advantage;
  }

  public List<Game> getGames() {
    return games;
  }

  public Integer getId() {
    return id;
  }

  public League getLeague() {
    return league;
  }

  public Integer getLeague_id() {
    return league_id;
  }

  public Live getLive() {
    return live;
  }

  public String getMatch_type() {
    return match_type;
  }

  public String getModified_at() {
    return modified_at;
  }

  public String getName() {
    return name;
  }

  public Integer getNumber_of_games() {
    return number_of_games;
  }

  public List<Opponent> getOpponents() {
    return opponents;
  }

  public String getOriginal_scheduled_at() {
    return original_scheduled_at;
  }

  public boolean isRescheduled() {
    return rescheduled;
  }

  public List<ResultObject> getResults() {
    return results;
  }

  public String getScheduled_at() {
    return scheduled_at;
  }

  public Serie getSerie() {
    return serie;
  }

  public Integer getSerie_id() {
    return serie_id;
  }

  public String getSlug() {
    return slug;
  }

  public String getStatus() {
    return status;
  }

  public List<Stream> getStreams_list() {
    return streams_list;
  }

  public Integer getTournament_id() {
    return tournament_id;
  }

  public Object getVideogame() {
    return videogame;
  }

  public VideogameVersion getVideogame_version() {
    return videogame_version;
  }

  public TeamObject getWinner() {
    return winner;
  }

  public Integer getWinner_id() {
    return winner_id;
  }

  public class Game {
    String begin_at;
    boolean complete;
    boolean detailed_stats;
    String end_at;
    boolean finished;
    boolean forfeit;
    Integer id;
    Integer length;
    Integer match_id;
    Integer position;
    String status;
    Winner winner;
    String winner_type;

    public Game(
        String begin_at,
        boolean complete,
        boolean detailed_stats,
        String end_at,
        boolean finished,
        boolean forfeit,
        Integer id,
        Integer length,
        Integer match_id,
        Integer position,
        String status,
        Winner winner,
        String winner_type) {
      this.begin_at = begin_at;
      this.complete = complete;
      this.detailed_stats = detailed_stats;
      this.end_at = end_at;
      this.finished = finished;
      this.forfeit = forfeit;
      this.id = id;
      this.length = length;
      this.match_id = match_id;
      this.position = position;
      this.status = status;
      this.winner = winner;
      this.winner_type = winner_type;
    }

    public String getBegin_at() {
      return begin_at;
    }

    public boolean isComplete() {
      return complete;
    }

    public boolean isDetailed_stats() {
      return detailed_stats;
    }

    public String getEnd_at() {
      return end_at;
    }

    public boolean isFinished() {
      return finished;
    }

    public boolean isForfeit() {
      return forfeit;
    }

    public Integer getId() {
      return id;
    }

    public Integer getLength() {
      return length;
    }

    public Integer getMatch_id() {
      return match_id;
    }

    public Integer getPosition() {
      return position;
    }

    public String getStatus() {
      return status;
    }

    public Winner getWinner() {
      return winner;
    }

    public String getWinner_type() {
      return winner_type;
    }
  }

  public class Winner {
    Integer id;
    String type;

    public Winner(Integer id, String type) {
      this.id = id;
      this.type = type;
    }

    public Integer getId() {
      return id;
    }

    public String getType() {
      return type;
    }
  }

  public class League {
    Integer id;
    String image_url;
    String modified_at;
    String name;
    String slug;
    String url;

    public League(
        Integer id, String image_url, String modified_at, String name, String slug, String url) {
      this.id = id;
      this.image_url = image_url;
      this.modified_at = modified_at;
      this.name = name;
      this.slug = slug;
      this.url = url;
    }

    public Integer getId() {
      return id;
    }

    public String getImage_url() {
      return image_url;
    }

    public String getModified_at() {
      return modified_at;
    }

    public String getName() {
      return name;
    }

    public String getSlug() {
      return slug;
    }

    public String getUrl() {
      return url;
    }
  }

  public class Live {
    String opens_at;
    boolean supported;
    String url;

    public Live(String opens_at, boolean supported, String url) {
      this.opens_at = opens_at;
      this.supported = supported;
      this.url = url;
    }

    public String getOpens_at() {
      return opens_at;
    }

    public boolean isSupported() {
      return supported;
    }

    public String getUrl() {
      return url;
    }
  }

  public class Opponent {
    TeamObject opponent; // two options
    String type;

    public Opponent(TeamObject opponent, String type) {
      this.opponent = opponent;
      this.type = type;
    }

    public TeamObject getOpponent() {
      return opponent;
    }

    public String getType() {
      return type;
    }
  }

  public class TeamObject {
    String acronym;
    Integer id;
    String image_url;
    String location;
    String modified_at;
    String name;
    String slug;

    public TeamObject(
        String acronym,
        Integer id,
        String image_url,
        String location,
        String modified_at,
        String name,
        String slug) {
      this.acronym = acronym;
      this.id = id;
      this.image_url = image_url;
      this.location = location;
      this.modified_at = modified_at;
      this.name = name;
      this.slug = slug;
    }

    public String getAcronym() {
      return acronym;
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

    public String getSlug() {
      return slug;
    }
  }

  public class ResultObject { // two options
    Integer score;
    Integer team_id;

    public ResultObject(Integer score, Integer team_id) {
      this.score = score;
      this.team_id = team_id;
    }

    public Integer getScore() {
      return score;
    }

    public Integer getTeam_id() {
      return team_id;
    }
  }

  public class Serie {
    String begin_at;
    String description;
    String end_at;
    String full_name;
    Integer id;
    Integer league_id;
    String modified_at;
    String name;
    String season;
    String slug;
    Integer winner_id;
    String winner_type;
    Integer year;

    public Serie(
        String begin_at,
        String description,
        String end_at,
        String full_name,
        Integer id,
        Integer league_id,
        String modified_at,
        String name,
        String season,
        String slug,
        Integer winner_id,
        String winner_type,
        Integer year) {
      this.begin_at = begin_at;
      this.description = description;
      this.end_at = end_at;
      this.full_name = full_name;
      this.id = id;
      this.league_id = league_id;
      this.modified_at = modified_at;
      this.name = name;
      this.season = season;
      this.slug = slug;
      this.winner_id = winner_id;
      this.winner_type = winner_type;
      this.year = year;
    }

    public String getBegin_at() {
      return begin_at;
    }

    public String getDescription() {
      return description;
    }

    public String getEnd_at() {
      return end_at;
    }

    public String getFull_name() {
      return full_name;
    }

    public Integer getId() {
      return id;
    }

    public Integer getLeague_id() {
      return league_id;
    }

    public String getModified_at() {
      return modified_at;
    }

    public String getName() {
      return name;
    }

    public String getSeason() {
      return season;
    }

    public String getSlug() {
      return slug;
    }

    public Integer getWinner_id() {
      return winner_id;
    }

    public String getWinner_type() {
      return winner_type;
    }

    public Integer getYear() {
      return year;
    }
  }

  public class Stream {
    String embed_url;
    String language;
    boolean main;
    boolean official;
    String raw_url;

    public Stream(
        String embed_url, String language, boolean main, boolean official, String raw_url) {
      this.embed_url = embed_url;
      this.language = language;
      this.main = main;
      this.official = official;
      this.raw_url = raw_url;
    }

    public String getEmbed_url() {
      return embed_url;
    }

    public String getLanguage() {
      return language;
    }

    public boolean isMain() {
      return main;
    }

    public boolean isOfficial() {
      return official;
    }

    public String getRaw_url() {
      return raw_url;
    }
  }

  public class Tournament {
    String begin_at;
    String end_at;
    Integer id;
    Integer league_id;
    boolean live_supported;
    String modified_at;
    String name;
    String prizepool;
    Integer serie_id;
    String slug;
    String tier;
    Integer winner_id;
    String winner_type;

    public Tournament(
        String begin_at,
        String end_at,
        Integer id,
        Integer league_id,
        boolean live_supported,
        String modified_at,
        String name,
        String prizepool,
        Integer serie_id,
        String slug,
        String tier,
        Integer winner_id,
        String winner_type) {
      this.begin_at = begin_at;
      this.end_at = end_at;
      this.id = id;
      this.league_id = league_id;
      this.live_supported = live_supported;
      this.modified_at = modified_at;
      this.name = name;
      this.prizepool = prizepool;
      this.serie_id = serie_id;
      this.slug = slug;
      this.tier = tier;
      this.winner_id = winner_id;
      this.winner_type = winner_type;
    }

    public String getBegin_at() {
      return begin_at;
    }

    public String getEnd_at() {
      return end_at;
    }

    public Integer getId() {
      return id;
    }

    public Integer getLeague_id() {
      return league_id;
    }

    public boolean isLive_supported() {
      return live_supported;
    }

    public String getModified_at() {
      return modified_at;
    }

    public String getName() {
      return name;
    }

    public String getPrizepool() {
      return prizepool;
    }

    public Integer getSerie_id() {
      return serie_id;
    }

    public String getSlug() {
      return slug;
    }

    public String getTier() {
      return tier;
    }

    public Integer getWinner_id() {
      return winner_id;
    }

    public String getWinner_type() {
      return winner_type;
    }
  }

  public class VideogameVersion {
    boolean current;
    String name;

    public VideogameVersion(boolean current, String name) {
      this.current = current;
      this.name = name;
    }

    public boolean isCurrent() {
      return current;
    }

    public String getName() {
      return name;
    }
  }
}
