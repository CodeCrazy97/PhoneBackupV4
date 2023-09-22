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
            = "phone_backup_v4.db";

    public Database() throws SQLException {
        this.basePath
                = new File("").getAbsolutePath();
        this.connection
                = this.connect();
    }

    private Connection connect() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
        } catch (SQLException sqle) {
            System.out.println("Trouble checking isClosed");
        }

        String url
                = "jdbc:sqlite:" + basePath.replace("\\",
                        "/") + "/" + dbName;
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

    public boolean createContactsTable() {
        String sql
                = "CREATE TABLE IF NOT EXISTS contacts (name TEXT NOT NULL,\n"
                + "phone_number INTEGER,\n"
                + "PRIMARY KEY (name, phone_number));";

        try {
            Statement stmt
                    = this.connection.createStatement();
            stmt.execute(sql);
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
