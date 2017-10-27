/*
 * IST 440W Team 6
 * Project Savage Enigma
 */
package translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONArray;

/**
 *
 * @author Youngmin Son
 */
public class Translator {
    
    public static class GoogleTranslate {
        /**
         * Translates a given text to a user-chosen language
         * @param textToTranslate
         * @param targetLanguage
         * @return Translated string
         * @throws java.io.UnsupportedEncodingException
         * @throws java.net.MalformedURLException
         */
        public static String translateText(String textToTranslate, String targetLanguage) 
                throws UnsupportedEncodingException, MalformedURLException, IOException {
            // Used example codes from http://archana-testing.blogspot.com/2016/02/calling-google-translation-api-in-java.html
            String url = "https://translate.googleapis.com/translate_a/single?"+ "client=gtx&"+
                    "sl=" + "auto" + "&tl=" + targetLanguage + "&dt=t&q=" + URLEncoder.encode(textToTranslate, "UTF-8");    
            URL obj;
            try {
                obj = new URL(url);
            } catch (MalformedURLException ex) {
                System.out.println("Error occured: " + ex.toString());
                return "Cannot Connect to the Web";
            }
            HttpURLConnection con = (HttpURLConnection) obj.openConnection(); 
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();
            JSONArray jsonArray = new JSONArray(response.toString());
            JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
            JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);
            System.out.println("Text to translate: " + textToTranslate);
            System.out.println("Translate to: " + targetLanguage);
            System.out.println("Translated text: " + jsonArray3.get(0).toString());
            return jsonArray3.get(0).toString();
        }
        
    }
    
}
