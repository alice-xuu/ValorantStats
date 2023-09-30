package valorantstats.model;

import valorantstats.model.enums.CallResult;

import java.io.File;
import java.sql.*;
import java.util.AbstractMap.SimpleEntry;

/**
 * Database containing user information and caching
 */
public class Database {

    private static final String dbName = "pandascore.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    public void createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            System.out.println("Database already created");
            return;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            System.out.println("A new database has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void removeDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            boolean result = dbFile.delete();
            if (!result) {
                System.out.println("Couldn't delete existing db file");
                System.exit(-1);
            } else {
                System.out.println("Removed existing DB file.");
            }
        } else {
            System.out.println("No existing DB file.");
        }
    }

    public void setupDB() {
        String createTeamTableSQL =
                """
                CREATE TABLE IF NOT EXISTS TeamResults (
                    id integer PRIMARY KEY,
                    teamName TEXT,
                    results TEXT
                )
                """;

        String createMatchTableSQL =
                """
                CREATE TABLE IF NOT EXISTS MatchResults (
                    id integer PRIMARY KEY,
                    teamId TEXT,
                    results TEXT
                )
                """;

        String createAccountsTableSQL =
                """
                CREATE TABLE IF NOT EXISTS Accounts (
                    id integer PRIMARY KEY,
                    username TEXT,
                    password TEXT,
                    backgroundColor TEXT,
                    textColor TEXT,
                    buttonColor TEXT,
                    highlightColor TEXT, 
                    breadcrumb TEXT,
                    breadcrumbIndex integer,
                    callResult TEXT,
                    currentTeamOrMatch
                )
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(createTeamTableSQL);
            statement.execute(createMatchTableSQL);
            statement.execute(createAccountsTableSQL);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void saveTeamResult(String teamName, String results) {
        String saveTR =
                """
                INSERT INTO TeamResults(teamName, results) VALUES
                    (?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(saveTR)) {
            preparedStatement.setString(1, teamName);
            preparedStatement.setString(2, results);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void saveMatchResult(String teamId, String results) {
        String saveMR =
                """
                INSERT INTO MatchResults(teamId, results) VALUES
                    (?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(saveMR)) {
            preparedStatement.setString(1, teamId);
            preparedStatement.setString(2, results);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public SimpleEntry<CallResult, String> queryTeam(String teamName) {
        String queryTeam =
                """
                SELECT results
                FROM TeamResults
                WHERE teamName = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(queryTeam)) {
            preparedStatement.setString(1, teamName);

            ResultSet results = preparedStatement.executeQuery();

            SimpleEntry<CallResult, String> data = null;
            if (results.next()) {
                data = new SimpleEntry<>(CallResult.CACHEHIT, results.getString("results"));
            }

            return data;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public SimpleEntry<CallResult, String> queryMatch(String teamId) {
        String queryMatch =
                """
                SELECT results
                FROM MatchResults
                WHERE teamId = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(queryMatch)) {
            preparedStatement.setString(1, teamId);

            ResultSet results = preparedStatement.executeQuery();

            SimpleEntry<CallResult, String> data = null;
            if (results.next()) {
                data = new SimpleEntry<>(CallResult.CACHEHIT, results.getString("results"));
            }

            return data;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public void updateTeam(String teamName, String results) {
        String addS =
                """
                UPDATE TeamResults
                SET results = ?
                WHERE teamName = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, results);
            preparedStatement.setString(2, teamName);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void updateMatch(String teamId, String results) {
        String addS =
                """
                UPDATE MatchResults
                SET results = ?
                WHERE teamId = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, results);
            preparedStatement.setString(2, teamId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    // User account functions

    public void addAccount(String username, String password) {
        String addU =
                """
                INSERT INTO Accounts(username, password) VALUES
                    (?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addU)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }


    public boolean queryAccount(String username, String password) {
        String qs =
                """
                SELECT *
                FROM Accounts
                WHERE username = ? AND password = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(qs)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet results = preparedStatement.executeQuery();

            return results.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return false;
    }

    public boolean queryUsername(String username) {
        String qs =
                """
                SELECT *
                FROM Accounts
                WHERE username = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(qs)) {
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();

            return results.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return false;
    }

    // personalisation functions

    public void updateBackgroundColor(String username, String color) {
        String addS =
                """
                UPDATE Accounts
                SET backgroundColor = ?
                WHERE username = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, color);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void updateTextColor(String username, String color) {
        String addS =
                """
                UPDATE Accounts
                SET textColor = ?
                WHERE username = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, color);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void updateButtonColor(String username, String color) {
        String addS =
                """
                UPDATE Accounts
                SET buttonColor = ?
                WHERE username = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, color);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void updateBreadcrumb(String username, String breadcrumb, int breadcrumbIndex) {
        String addS =
                """
                UPDATE Accounts
                SET breadcrumb = ?, breadcrumbIndex = ?
                WHERE username = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, breadcrumb);
            preparedStatement.setInt(2, breadcrumbIndex);
            preparedStatement.setString(3, username);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void updateCurrentTeamOrMatch(String username, String callResult, String currentTeamOrMatch) {
        String addS =
                """
                UPDATE Accounts
                SET callResult = ?, currentTeamOrMatch = ?
                WHERE username = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, callResult);
            preparedStatement.setString(2, currentTeamOrMatch);
            preparedStatement.setString(3, username);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public String queryBreadcrumb(String username) {
        String qs =
                """
                SELECT breadcrumb
                FROM Accounts
                WHERE username = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(qs)) {
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()){
                String breadcrumb = results.getString("breadcrumb");
                return breadcrumb;
            }
            else{
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public Integer queryBreadcrumbIndex(String username) {
        String qs =
                """
                SELECT breadcrumbIndex
                FROM Accounts
                WHERE username = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(qs)) {
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()){
                int breadcrumbIndex = results.getInt("breadcrumbIndex");
                return breadcrumbIndex;
            }
            else{
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public SimpleEntry<String, String> queryCall(String username) {
        String qs =
                """
                SELECT callResult, currentTeamOrMatch
                FROM Accounts
                WHERE username = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(qs)) {
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()){
                SimpleEntry<String, String> result = new SimpleEntry<>(results.getString("callResult"), results.getString("currentTeamOrMatch"));
                return result;
            }
            else{
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public String queryComponentColor(String username, String component) {
        String qs =
                """
                SELECT *
                FROM Accounts
                WHERE username = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(qs)) {
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()){
                String colorString = results.getString(component);
                return colorString;
            }
            else{
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public String queryHighlightColors(String username) {
        String qs =
                """
                SELECT highlightColor
                FROM Accounts
                WHERE username = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(qs)) {
            preparedStatement.setString(1, username);

            ResultSet results = preparedStatement.executeQuery();

            if (results.next()){
                String colorString = results.getString("highlightColor");
                return colorString;
            }
            else{
                return null;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public void clearCache() {
        String dropTeam =
                """
                DROP TABLE TeamResults;
                """;
        String dropMatch =
                """
                DROP TABLE MatchResults;
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(dropTeam);
            statement.execute(dropMatch);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        setupDB();
    }

    public void updateHighlightColors(String currentUser, String colorString) {
        String addS =
                """
                UPDATE Accounts
                SET highlightColor = ?
                WHERE username = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addS)) {
            preparedStatement.setString(1, colorString);
            preparedStatement.setString(2, currentUser);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }
}
