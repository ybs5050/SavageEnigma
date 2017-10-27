/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package database;

import cipher.Hash;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User table database transaction class (static)
 * @author Youngmin
 */
public class Users {
    
    public static class UserHandler {
        
        private static Connection conn = Database.conn;
        private static Statement stmt = Database.stmt;
        
        // user information
        public static int userId = 0;
        
        /**
         * Register a user function
         * @param userame
         * @param password
         * Used example codes from https://db.apache.org/derby/integrate/plugin_help/derby_app.html
         * @return 0 = Success, 1 = Username already exists
         * @throws java.sql.SQLException
         * @throws java.security.NoSuchAlgorithmException
         */
        public static int registerUser(String userame, String password) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
            // Used example codes from https://netbeans.org/kb/docs/ide/java-db.html?print=yes
            Hash hash = new Hash(password);
            password = hash.getSha1();
            String currentDate = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
            System.out.println("SHA1 Password: " + password);
            stmt = conn.createStatement();
            String statement = "INSERT INTO " + "APP.\"users\" " +
                    "VALUES (" + "DEFAULT, " + "\'" + userame + "\', " + "\'" + password + "\', " +
                    "\'" + currentDate + "\', true"+ ")";
            System.out.println(statement);
            try {
                stmt.execute(statement);
                stmt.close();   
                return 0;
            } catch (SQLException except) {
                System.out.println("Error occured: " + except.toString());
                return 1;
            }
        }
        
        /**
         * Check whether a given username and password matches the database record
         * @param username
         * @param password
         * @return 0 = username and password matches, 1 = password does not match, 2 = username does not exist, 3 = failed to connect to db
         * @throws java.sql.SQLException
         * @throws java.security.NoSuchAlgorithmException
         */
        public static int authenticateUser(String username, String password) throws SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
            // Used example codes from https://netbeans.org/kb/docs/ide/java-db.html?print=yes
            // Execute statement and see if username and password matches the database
            Hash hash = new Hash(password);
            password = hash.getSha1();
            try {
                stmt = conn.createStatement();
                String statement = "SELECT * FROM APP.\"users\" WHERE username = \'" + username + "\'";
                System.out.println(statement);
                ResultSet stmtResult = stmt.executeQuery(statement);
                
                if (!stmtResult.next()) {
                    // username does not exist in the database
                    return 2;
                } else {
                    // ID = 1, USERNAME = 2, PASSWORD = 3 
                    // PASSWORD column is position 3 in the "users" table
                    String returnedPassword = stmtResult.getString(3);
                    if (returnedPassword.equals(password)) {
                        // Password matches
                        userId = stmtResult.getInt(1);
                        stmtResult.close();
                        stmt.close();
                        return 0;
                    } else {
                        // Password does not match
                        stmtResult.close();
                        stmt.close();
                        return 1;
                    }
                }
            } catch (SQLException except) {
                System.out.println("Error occured: " + except.toString());
                return 3;
            }
        }
    }
}


