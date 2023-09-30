package valorantstats.model;

import valorantstats.model.enums.CallResult;

import java.util.AbstractMap.SimpleEntry;

public interface CacheManager {
  Database getDb();

  /**
   * Saves the retrieved team results in the database
   *
   * @param teamName name of team to be cached
   * @param results string in json format representing team information
   */
  void cacheTeam(String teamName, String results);

  /**
   * Saves the retrieved match results in the database
   *
   * @param teamId id of a team to be cached
   * @param results string in json format representing matches
   */
  void cacheMatch(String teamId, String results);

  /**
   * Checks for a cache hit in the database for a team if the input api is online
   *
   * @param teamName name of team to be queried
   * @return ERROR if input api is offline, input is invalid, or no cache is found, SUCCESS if a
   *     cache hit was found
   */
  SimpleEntry<CallResult, String> searchCacheTeam(String teamName);

  /**
   * Checks for a cache hit in the database for a team id if the input api is online
   *
   * @param teamId id of team to be queried
   * @return ERROR if input api is offline, input is invalid, or no cache is found, SUCCESS if a
   *     cache hit was found
   */
  SimpleEntry<CallResult, String> searchCacheMatch(String teamId);

  /**
   * Updates the database with new results for a team
   *
   * @param teamName name of team of database entry to be updated
   * @param results string in json format representing team information
   */
  void updateCacheTeam(String teamName, String results);

  /**
   * Updates the database with new results for matches
   *
   * @param teamId id of team of database entry to be updated
   * @param results string in json format representing match information
   */
  void updateCacheMatch(String teamId, String results);

  /**
   * Checks if the cached result was for a team that exists or if the cached data was for a
   * nonexistent team
   *
   * @param cacheResult result retrieved from a db query in json format
   * @return ERROR if result isn't team data, SUCCESS if it is
   */
  SimpleEntry<CallResult, String> teamVerifyCachedResult(
      SimpleEntry<CallResult, String> cacheResult);

  /**
   * Checks if the cached result was for a team id with matches that exists or not
   *
   * @param cacheResult result retrieved from a db query in json format
   * @return ERROR if result isn't match data, SUCCESS if it is
   */
  SimpleEntry<CallResult, String> matchVerifyCachedResult(
      SimpleEntry<CallResult, String> cacheResult);

  /** Clears the team and match cache tables */
  void clearCache();
}
