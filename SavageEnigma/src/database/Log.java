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
    
    /**
     * This is the default constructor for the Log Class
     * @param encryptedText
     * @param decryptedText 
     */
    public Log(String encryptedText, String decryptedText) {
        this.encryptedText = new SimpleStringProperty(encryptedText);
        this.decryptedText = new SimpleStringProperty(decryptedText);
    }
    
    public String getEncryptedText() {
        return encryptedText.get();
    }
    
    public String getDecryptedText() {
        return decryptedText.get();
    }
}
