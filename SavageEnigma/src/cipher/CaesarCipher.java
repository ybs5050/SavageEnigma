/*
 * IST 440W Team 6
 * Project Savage Enigma
 */

package cipher;

import java.util.ArrayList;

/**
 * Class responsible for decrypting a parsed text from a PNG image file
 * @author Youngmin
 */
public class CaesarCipher {
    
    private String encryptedText;
    private String decryptedText;
    private ArrayList<String> decryptList; 
    
    /**
     * This is the default constructor for this class
     * @param encryptedText 
     */
    public CaesarCipher(String encryptedText) {
        this.encryptedText = encryptedText;
    }
    
    /**
     * Second constructor
     * @param encryptedText
     * @param decryptedText 
     */
    public CaesarCipher(String encryptedText, String decryptedText) {
        this.decryptedText = decryptedText;
    }
    
    /**
     * Returns a observable list of this class 
     * @return ArrayList<String> decryptList
     */
    public ArrayList<String> getDecryptedTextList() {
        if (decryptList.isEmpty()) {
            return null;
        } else {
            return decryptList;
        }
    }
    
    /**
     * Decrypts the cipher
     */
    public void decryptCipher() {
        StringBuilder sb = new StringBuilder();
        int alphabetCount = 26;
        System.out.println("Decrypted Ciphers:");
        decryptList = new ArrayList<String>();
        for (int j = 1; j <= alphabetCount; j++) {
            for(int i = 0; i < encryptedText.length(); i++) {
                char temp = (char) (encryptedText.charAt(i) - j);
                sb.append(temp);
            }
            System.out.println(j + " " + sb.toString());
            decryptList.add(sb.toString());
            sb = new StringBuilder();
        }
    }

}
