/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package database;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;



/**
 * Database handling class (static) 
 * @author Youngmin
 */
public class Database {
    
    // jdbc information
    private static final String dbURL = "jdbc:derby://localhost:1527/savage_enigma";
    public static Connection conn = null;
    public static Statement stmt = null;
    
    /**
     * Static class for handling db operations
     */
    public static class DatabaseHandler {
        
        /**
         * Establishes a connection to the schema
         * @throws ClassNotFoundException 
         * Used example codes from https://db.apache.org/derby/integrate/plugin_help/derby_app.html
         */
        public static void connectDatabase() throws ClassNotFoundException {
            // Used example codes from https://netbeans.org/kb/docs/ide/java-db.html?print=yes
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                // Establish connection
                conn = DriverManager.getConnection(dbURL);
                System.out.println("Connection to database established. Schema name: "
                + conn.getSchema());
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
                System.out.println("Error occured: " + except.toString());
            }
        }
        
        /**
         * Prints content of a given table name
         * @param tableName table name to be printed
         * @throws SQLException 
         * Used example codes from https://db.apache.org/derby/integrate/plugin_help/derby_app.html
         */
        public static void printTable(String tableName) throws SQLException {
            // Used example codes from https://netbeans.org/kb/docs/ide/java-db.html?print=yes
            try {
                stmt = conn.createStatement();
                ResultSet stmtResult = stmt.executeQuery("SELECT * FROM " + "APP." + "\"" + tableName + "\"");
                ResultSetMetaData rsMetaData = stmtResult.getMetaData();
                int numberCols = rsMetaData.getColumnCount();
                
                // null return check
                if (!stmtResult.next()) {
                    System.out.println("No records found");
                } else {
                    // print Column Names
                    for (int i = 1; i <= numberCols; i++) {
                        System.out.print(rsMetaData.getColumnLabel(i)+"\t\t");  
                    }
                    System.out.println();
                    
                    // print each row
                    do {
                        for (int i = 1; i <= numberCols; i++) {
                            System.out.print(stmtResult.getString(i) + "\t\t");
                        }
                        System.out.println();
                    } while (stmtResult.next()); 
                    stmtResult.close();
                    stmt.close();
                }
  
            } catch (SQLException except) {
                System.out.println("Error occured: " + except.toString());
            }
        }
    }
}
