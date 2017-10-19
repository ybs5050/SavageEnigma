/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package cipher;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Class responsible for creating sha-1 hash for user password
 * @author Youngmin
 */
public class Hash {
    
    private String password;
    
    /**
     * This is the default constructor for this class
     * @param password 
     */
    public Hash(String password) {
        this.password = password;
    } 
    
    /**
     * Return a string value of sha1 hashed password
     * @return Hashed String
     * @throws NoSuchAlgorithmException 
     * @throws java.io.UnsupportedEncodingException 
     */
    public String getSha1() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // Used example code from http://www.java2s.com/Tutorial/Java/0490__Security/Encryptapassword.htm
        java.security.MessageDigest sha1 = java.security.MessageDigest.getInstance("SHA-1");
        sha1.update(password.getBytes("UTF-8"));
        return new sun.misc.BASE64Encoder().encode(sha1.digest());
    }

}
