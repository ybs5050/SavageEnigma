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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



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
         * @return true = db connection established, false = failed to connect to db
         * @throws ClassNotFoundException 
         * Used example codes from https://db.apache.org/derby/integrate/plugin_help/derby_app.html
         */
        public static boolean connectDatabase() throws ClassNotFoundException {
            // Used example codes from https://netbeans.org/kb/docs/ide/java-db.html?print=yes
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
                // Establish connection
                conn = DriverManager.getConnection(dbURL);
                System.out.println("Connection to database established. Schema name: "
                + conn.getSchema());
                return true;
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException except) {
                System.out.println("Error occured: " + except.toString());
                return false;
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
        
        /**
         * Insert parsed text to the db
         * @param encryptedText
         * @throws SQLException 
         */
        public static void insertEncryptedText(String encryptedText) throws SQLException {
            // Used example codes from https://netbeans.org/kb/docs/ide/java-db.html?print=yes
            // Create workflow first
            String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
            stmt = conn.createStatement();
            String statement = "INSERT INTO " + "APP.\"workflow\" " +
                    "VALUES (" + "DEFAULT, " + database.Users.UserHandler.userId + ", false, " + 
                    "\'" + currentDate + "\'" + ")";
            try {
                stmt.execute(statement);
                stmt.close();   
            } catch (SQLException except) {
                System.out.println("Error occured: " + except.toString());
            }
            System.out.println(statement);
            // Get recent workflow ID
            stmt = conn.createStatement();
            System.out.println("SELECT * FROM APP.\"workflow\" ORDER BY ID DESC");
            ResultSet stmtResult = stmt.executeQuery("SELECT (ID) FROM APP.\"workflow\" ORDER BY ID DESC");
            int workflowId = 0;
            if(stmtResult.next()) {
                workflowId = stmtResult.getInt(1);
            }
            stmt.close();
            // Insert to encrypted_text table
            stmt = conn.createStatement();
            statement = "INSERT INTO " + "APP.\"encrypted_text\" " + 
                    "VALUES (" + "DEFAULT, " + workflowId + ", " + "\'" + encryptedText + "\', false, " +
                    "\'" + currentDate + "\'" + ")";
            System.out.println(statement);
            stmt.execute(statement);
            stmt.close();
            // Insert to logs table
            stmt = conn.createStatement();
            statement = "INSERT INTO " + "APP.\"logs\" " + 
                    "VALUES (" + "DEFAULT, " + database.Users.UserHandler.userId + ", " + workflowId + 
                    ", " + "\'" + encryptedText + "\', NULL" + ")";
            System.out.println(statement);
            stmt.execute(statement);
            stmt.close();   
            
        }
        
        /**
         * Fetch all logs from the db
         * @return ObservableList<Log>
         * @throws SQLException 
         */
        public static ObservableList<Log> getAllLogs() throws SQLException {
            // Used example codes from https://netbeans.org/kb/docs/ide/java-db.html?print=yes
            ArrayList<Log> logList = new ArrayList<>();
            stmt = conn.createStatement();
            ResultSet stmtResult = stmt.executeQuery("SELECT * FROM APP.\"logs\"");
            if(!stmtResult.next()) {
                return FXCollections.observableArrayList(logList);
            } else {
                do {
                    int logId = stmtResult.getInt(1); // logId
                    String encryptedText= stmtResult.getString(4); // encrypted text
                    String decryptedText = stmtResult.getString(5); // decrypted text
                    logList.add(new Log(logId, encryptedText, decryptedText));
                } while(stmtResult.next());
                stmt.close();
                return FXCollections.observableArrayList(logList);
            }
        }
        
        /**
         * Update log by inserting decrypted text to the existing row
         * @param logId
         * @param decryptedText
         * @return true = success, false = failure
         * @throws java.sql.SQLException
         */
        public static boolean updateLog(String logId, String decryptedText) throws SQLException {
            // get WORKFLOW_ID of logs table by logId
            stmt = conn.createStatement();
            System.out.println("SELECT (WORKFLOW_ID) FROM APP.\"logs\" WHERE ID = " + logId);
            ResultSet stmtResult = stmt.executeQuery("SELECT (WORKFLOW_ID) FROM APP.\"logs\" WHERE ID = " + logId);
            int workflowId = 0;
            if(stmtResult.next()) {
                workflowId = stmtResult.getInt(1);
            }
            stmt.close();
            // get ID of table encrypted_text that matches WORKFLOW_ID workflowId
            stmt = conn.createStatement();
            System.out.println("SELECT (ID) FROM APP.\"encryptedText\" WHERE WORKFLOW_ID = " + workflowId);
            stmtResult = stmt.executeQuery("SELECT (ID) FROM APP.\"encrypted_text\" WHERE WORKFLOW_ID = " + workflowId);
            int encryptedId = 0;
            if(stmtResult.next()) {
                encryptedId = stmtResult.getInt(1);
            }
            stmt.close();
            // insert into decrypted_text table
            String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
            stmt = conn.createStatement();
            String statement = "INSERT INTO " + "APP.\"decrypted_text\" " + 
                    "VALUES (" + "DEFAULT, " + encryptedId + ", " + "\'" + decryptedText + "\', true, " +
                    "\'" + currentDate + "\'" + ")";  
            System.out.println(statement);
            stmt.execute(statement);
            stmt.close();
            // update encrypted_text table
            stmt = conn.createStatement();
            statement = "UPDATE " + "APP.\"encrypted_text\" " +
                    "SET STATUS = true" + " WHERE ID = " + encryptedId;
            System.out.println(statement);
            stmt.execute(statement);
            stmt.close();
            // update workflow table
            stmt = conn.createStatement();
            statement = "UPDATE " + "APP.\"workflow\" " +
                    "SET STATUS = true" + " WHERE ID = " + workflowId;
            System.out.println(statement);
            stmt.execute(statement);
            stmt.close();
            // update logs table
            stmt = conn.createStatement();
            statement = "UPDATE " + "APP.\"logs\" " +
                    "SET DECRYPTED_TEXT = \'" + decryptedText + "\'" + " WHERE ID = " + logId;
            System.out.println(statement);
            stmt.execute(statement);
            stmt.close();
            return true;
        }
        
        /**
         * Deletes a user selected log(row) from the dataabase
         * @param logId
         * @return true = success, false = failure
         * @throws SQLException 
         */
        public static boolean deleteLog(String logId) throws SQLException {
            stmt = conn.createStatement();
            String statement = "DELETE FROM APP.\"logs\" WHERE ID = " + logId;
            System.out.println(statement);
            try {
                stmt.execute(statement);
                stmt.close();
                return true;
            } catch (SQLException except) {
                System.out.println("Error occured: " + except.toString());
                return false;
            }
        }
    }
}

