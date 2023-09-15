package phonebackupv4;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private Connection connection;
    private String basePath;
    private String dbName
            = "phone_backup_v2";

    public Database() throws SQLException {
        this.basePath
                = new File("").getAbsolutePath();

        String url
                = "jdbc:sqlite:" + basePath.replace("\\",
                        "/") + "/" + this.dbName;

        try (Connection conn
                = DriverManager.getConnection(url)) {
            if (conn != null) {
                this.connection = conn;
                DatabaseMetaData meta
                        = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        // SQLite connection string

        String url
                = "jdbc:sqlite:" + basePath.replace("\\",
                        "/") + "/funny.db";
        Connection conn
                = null;
        try {
            conn
                    = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void selectAll() {
        String sql
                = "SELECT age, name, weight FROM people";

        try (Connection conn
                = this.connect();
                Statement stmt
                = conn.createStatement();
                ResultSet rs
                = stmt.executeQuery(sql)) {

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("age") + "\t"
                        + rs.getString("name") + "\t"
                        + rs.getDouble("weight"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean dropDatabase() {
        File myObj
                = new File(basePath + "\\" + dbName);
        if (myObj.delete()) {
            return true;
        } else {
            return false;
        }
    }

}
