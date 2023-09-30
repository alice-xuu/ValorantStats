package valorantstats.model;

import com.google.gson.Gson;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;

import java.util.AbstractMap.SimpleEntry;

/** Implementation of CacheManager to manage database queries */
public class CacheManagerImpl implements CacheManager {

  private final Database db;

  public CacheManagerImpl(Database db) {
    this.db = db;
  }

  @Override
  public Database getDb() {
    return db;
  }

  @Override
  public void cacheTeam(String teamName, String results) {
    db.saveTeamResult(teamName, results);
  }

  @Override
  public void cacheMatch(String teamId, String results) {
    db.saveMatchResult(teamId, results);
  }

  @Override
  public SimpleEntry<CallResult, String> searchCacheTeam(String teamName) {
    SimpleEntry<CallResult, String> results = db.queryTeam(teamName);
    if (results != null && results.getValue().equals("[]")) {
      results.setValue("No team found: " + teamName);
    }
    return results;
  }

  @Override
  public SimpleEntry<CallResult, String> searchCacheMatch(String teamId) {
    SimpleEntry<CallResult, String> results = db.queryMatch(teamId);
    if (results != null && results.getValue().equals("[]")) {
      results.setValue("No match found: " + teamId);
    }
    return results;
  }

  @Override
  public void updateCacheTeam(String teamName, String results) {
    db.updateTeam(teamName, results);
  }

  @Override
  public void updateCacheMatch(String teamId, String results) {
    db.updateMatch(teamId, results);
  }

  @Override
  public SimpleEntry<CallResult, String> teamVerifyCachedResult(
      SimpleEntry<CallResult, String> cacheResult) {
    try {
      Team[] teams = new Gson().fromJson(cacheResult.getValue(), Team[].class);
      Team team = teams[0];
      return cacheResult;
    } catch (Exception e) {
      return new SimpleEntry<>(CallResult.ERROR, cacheResult.getValue());
    }
  }

  @Override
  public SimpleEntry<CallResult, String> matchVerifyCachedResult(
      SimpleEntry<CallResult, String> cacheResult) {
    try {
      Match[] matches = new Gson().fromJson(cacheResult.getValue(), Match[].class);
      return cacheResult;
    } catch (Exception e) {
      return new SimpleEntry<>(CallResult.ERROR, cacheResult.getValue());
    }
  }

  @Override
  public void clearCache() {
    db.clearCache();
  }
}
