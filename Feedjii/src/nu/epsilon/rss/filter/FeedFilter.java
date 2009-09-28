package nu.epsilon.rss.filter;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import nu.epsilon.rss.profiles.ProfileManager.Profile;
import nu.epsilon.rss.rssmanager.FeedItem;
import nu.epsilon.rss.ui.exception.ExceptionHandler;

/**
 * Responsible for filter functionality applied to feed items.
 * Use this class to set specific filtering conditions like 'title' or
 * 'description' that is applied to all feed items before they are displayed
 * on screen.
 *
 * @author Pär Sikö
 */
public class FeedFilter implements Filter {

    private Logger logger = Logger.getLogger("nu.epsilon.rss.filter");
    private String title;
    private String description;
    private String link;
    private Date startDate;
    private Date endDate;
    private String quickSearch;
    private final ExceptionHandler handler = new ExceptionHandler();

    @Override
    public void setTitle(String title) {
        this.title = addRegExpSyntax(title);
    }

    @Override
    public void setDescription(String description) {
        this.description = addRegExpSyntax(description);
    }

    @Override
    public void setLink(String link) {
        this.link = addRegExpSyntax(link);
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public void setQuickSearch(String quickSearch) {
        // this is a special case that is used during demonstration to invoke
        // our special "error handler". 
        if (quickSearch != null && quickSearch.equals("_")) {
            handler.showExceptionOnGlassPane(new NullPointerException(
                    "NullPointerException"));
        } else {
            this.quickSearch = addRegExpSyntax(quickSearch);
        }
    }
    
    @Override
    public String getQuickSearch() {
        return quickSearch;
    }

    @Override
    public void setProfileFilter(Profile profile) {
//        setDescription(profile.getDescriptionFilter());
//        setEndDate(profile.getEndDateFilter());
//        setLink(profile.getLinkFilter());
//        setStartDate(profile.getStartDateFilter());
//        setTitle(profile.getTitleFilter());
        setQuickSearch(profile.getDescriptionFilter());
    }

    @Override
    public boolean matches(FeedItem item) {

        boolean matches = true;
        try {
            // regexp, searching
            //Title
            if (title != null && item.title != null) {
                matches = Pattern.matches(title.toLowerCase(), item.title.
                        toLowerCase());
                logger.logp(Level.FINEST, this.getClass().toString(), "matches",
                        "title " + item.title + " matched " + title + ": " +
                        matches);
            }
            // Description
            if (description != null && item.description != null) {
                matches |= Pattern.matches(description.toLowerCase(),
                        item.description.toLowerCase());
                logger.logp(Level.FINEST, this.getClass().toString(), "matches",
                        "description " + item.description + " matched " +
                        description + ": " + matches);
            }
            // Link
            if (link != null && item.link != null) {
                matches |= Pattern.matches(link.toLowerCase(), item.link.
                        toLowerCase());
            }
            // Quick search
            if (quickSearch != null && !quickSearch.trim().isEmpty() &&
                    item.title != null && item.description != null) {
                boolean titleMatches = Pattern.matches(quickSearch.toLowerCase(),
                        item.title.toLowerCase());
                boolean descriptionMatches = Pattern.matches(
                        quickSearch.toLowerCase(),
                        item.description.toLowerCase());
                matches = titleMatches || descriptionMatches;
                logger.logp(Level.FINEST, this.getClass().toString(), "matches",
                        "quick search " + item.description + " or " + item.title +
                        "matched"  + quickSearch + ": " + matches);
            }
            // Published Date
            if ((item.publishedDate != null) &&
                    (startDate != null) &&
                    item.publishedDate.before(startDate)) {
                matches = false;
            }
            if (item.publishedDate != null && endDate != null &&
                    item.publishedDate.after(endDate)) {
                matches = false;
            }
        } catch (Exception exc) {
            logger.logp(Level.WARNING, this.getClass().toString(), "matches",
                    "Error when matching feed item against filter.", exc);
        }
        return matches;

    }

    private String addRegExpSyntax(String regexp) {
        if (regexp != null) {
            regexp = regexp.replace("*", ".*");
            if (!regexp.endsWith(".*")) {
                regexp += ".*";
            }
            if (!regexp.startsWith(".*")) {
                regexp = ".*" + regexp;
            }
        }
        return regexp;
    }
}
