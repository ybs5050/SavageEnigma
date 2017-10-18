/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package database;

import javafx.beans.property.SimpleStringProperty;

/**
 * Object class for storing past submissions 
 * @author Youngmin
 */
public class Log {
    
    // Java Table properties
    private SimpleStringProperty encryptedText;
    private SimpleStringProperty decryptedText;
    private int logId;
    
    /**
     * This is the default constructor for the Log Class
     * @param logId 'ID' field in logs table
     * @param encryptedText 'ENCRYPTED_TEXT' field in logs table
     * @param decryptedText 'DECRYPTED_TEXT' field in logs table 
     */
    public Log(int logId, String encryptedText, String decryptedText) {
        this.logId = logId;
        this.encryptedText = new SimpleStringProperty(encryptedText);
        this.decryptedText = new SimpleStringProperty(decryptedText);
    }
    
    public int getLogId() {
        return logId;
    }
    
    public String getEncryptedText() {
        return encryptedText.get();
    }
    
    public String getDecryptedText() {
        return decryptedText.get();
    }
}
