package nu.epsilon.rss.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Removes invalid characters from strings.
 *
 * @author Pär Sikö
 */
public class InvalidCharacterCleaner {

    private Logger logger = Logger.getLogger("nu.epsilon.rss.common");
    public final static List<String> invalidCharacters = new ArrayList<String>();

    public InvalidCharacterCleaner() {
        invalidCharacters.add("\\n");
        invalidCharacters.add("<html>");
        invalidCharacters.add("</html>");
        invalidCharacters.add("<body>");
        invalidCharacters.add("</body>");
        invalidCharacters.add("<br>");
        invalidCharacters.add("<br />");
        invalidCharacters.add("<br/>");
//        invalidCharacters.add("");
    }

    /**
     * Removes invalid character sequences from string. An invalid sequence is 
     * typically a HTML specific string like "<br>".
     *
     * @param stringToClean string to remove invalid characters sequences from.
     * @return a string without invalid character sequences.
     */
    public String clean(final String stringToClean) {
        if (stringToClean == null) {
            return "";
        }
        String cleanString = stringToClean;
        try {
            for (String invalidString : invalidCharacters) {
                cleanString = cleanString.replaceAll(invalidString, "");
            }
        } catch (Exception exc) {
            logger.logp(Level.WARNING, this.getClass().toString(),
                    "clean",
                    "Unable cleaning string", exc);
        }
        return cleanString;
    }
}
