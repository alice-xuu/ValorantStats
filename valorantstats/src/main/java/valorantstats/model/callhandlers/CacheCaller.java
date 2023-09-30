package valorantstats.model.callhandlers;

import valorantstats.model.CacheManager;
import valorantstats.model.apicalls.InputApiCalls;
import valorantstats.model.enums.CallResult;

import java.util.AbstractMap.SimpleEntry;

/** Checks the cache for a database entry existence or is bypassed to call on ApiCaller */
public class CacheCaller extends Caller {

  public CacheCaller(CacheManager cacheManager, InputApiCalls inputApiCalls) {
    super(cacheManager, inputApiCalls);
  }

  /**
   * Checks the cache for a team name or pass to the next caller If cacheOverride is false, check
   * for a cache database entry for the team, if no entry exists, or cacheOverride is true, pass
   * onto the next caller
   *
   * @param name team name to be searched
   * @param cacheOverride true = bypass database check, false = check database
   * @return CallResult - CACHEHIT if database entry exists, String - either information for team or
   *     error message
   */
  @Override
  public SimpleEntry<CallResult, String> callTeam(String name, boolean cacheOverride) {
    if (cacheOverride) {
      return this.nextCaller.callTeam(name, true);
    }
    SimpleEntry<CallResult, String> result = cacheManager.searchCacheTeam(name);

    if (result == null) {
      return this.nextCaller.callTeam(name, false);
    }

    return result;
  }

  /**
   * Checks the cache for a team id or pass to the next caller If cacheOverride is false, check for
   * a cache database entry for the team id, if no entry exists, or cacheOverride is true, pass onto
   * the next caller
   *
   * @param name team id to be searched
   * @param cacheOverride true = bypass database check, false = check database
   * @return CallResult - CACHEHIT if database entry exists, String - either information for team or
   *     error message
   */
  @Override
  public SimpleEntry<CallResult, String> callMatch(String name, boolean cacheOverride) {
    if (cacheOverride) {
      return this.nextCaller.callMatch(name, true);
    }
    SimpleEntry<CallResult, String> result = cacheManager.searchCacheMatch(name);
    if (result == null) {
      return this.nextCaller.callMatch(name, false);
    }
    return result;
  }
}
