package nu.epsilon.rss.common;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Converts coded characters to a human readable format
 *
 * @author Pär Sikö
 */
public class CharConverter {

    private static final Map<String, String> conversionTable =
            new HashMap<String, String>();
    private static final Logger logger = Logger.getLogger("nu.epsilon.rss.common");


    static {
        conversionTable.put("&#34;", "\"");
        conversionTable.put("&#35;", "#");
        conversionTable.put("&#36;", "$");
        conversionTable.put("&#37;", "%");
        conversionTable.put("&#38;", "&");
        conversionTable.put("&#39;", "'");
        conversionTable.put("&#43;", "+");
        conversionTable.put("&#60;", "<");
        conversionTable.put("&#64;", "@");
        conversionTable.put("&#92;", "\\");
        conversionTable.put("&#94;", "^");
        conversionTable.put("&#126;", "~");
        conversionTable.put("&#128;", "€");
        conversionTable.put("&#169;", "©");
        conversionTable.put("&#196;", "Ä");
        conversionTable.put("&#197;", "Å");
        conversionTable.put("&#214;", "Ö");
        conversionTable.put("&#228;", "ä");
        conversionTable.put("&#229;", "å");
        conversionTable.put("&#246;", "ö");
        conversionTable.put("&#8211;", "–");
        conversionTable.put("&#8212;", "—");
        conversionTable.put("&#8216;", "‘");
        conversionTable.put("&#8217;", "’");
        conversionTable.put("&#8218;", "‚");
        conversionTable.put("&#8220;", "“");
        conversionTable.put("&#8221;", "”");
        conversionTable.put("&#8222;", "„");
        conversionTable.put("&#8226;", "•");
        conversionTable.put("&#8364;", "€");
        conversionTable.put("&#8482;", "™");
        conversionTable.put("&#232;", "è");
        conversionTable.put("&#32;", " ");
        conversionTable.put("&#233;", "é");
        conversionTable.put("&aring;", "å");
        conversionTable.put("&Aring;", "Å");
        conversionTable.put("&auml;", "ä");
        conversionTable.put("&Auml;", "Ä");
        conversionTable.put("&ouml;", "ö");
        conversionTable.put("&Ouml;", "Ö");
        conversionTable.put("&lt;","<");
        conversionTable.put("&gt;",">");
        conversionTable.put("&amp;","&");
        conversionTable.put("&quot;","\"");
        conversionTable.put("&aelig;", "æ");
        conversionTable.put("&AElig;", "Æ");
        conversionTable.put("&oslash;", "ø");
        conversionTable.put("&Oslash;", "Ø");
    }

    private CharConverter() {
    }

    /**
     * Converts all coded characters to human readable characters.
     *
     * @param stringToConvert string containing coded characters
     * @return a string where all coded characters have been replaced by human
     *          readable characters.
     */
    public static String convertChars(final String stringToConvert) {
        if (stringToConvert == null) {
            return "";
        }
        String convertedString = stringToConvert;
        try {
            for (String key : conversionTable.keySet()) {
                if (convertedString.contains(key)) {
                    convertedString = convertedString.replaceAll(key,
                            conversionTable.get(key));
                }
            }
        } catch (Exception exc) {
            logger.logp(Level.WARNING, CharConverter.class.toString(), "convertChars",
                    "Unable to convert coded character.", exc);
        }
        return convertedString;
    }
}
