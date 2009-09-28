/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.epsilon.rss.profiles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import nu.epsilon.rss.ui.backend.BackendUIImpl;

/**
 *
 * @author Pär Sikö
 */
public class ProfileManager implements Serializable {

    private List<Profile> profiles = new ArrayList<Profile>();
    private Profile activeProfile;

    public ProfileManager() {
        Profile profile = new Profile("", "All");
        profile.setTitleFilter("");
        profile.setDescriptionFilter("");
        profiles.add(profile);
        activeProfile = profile;

        // Stockholm
        profile = new Profile("", "Swing");
        profile.setTitleFilter("Swing");
        profile.setDescriptionFilter("Swing");
        profiles.add(profile);

        // Obama
        profile = new Profile("", "Feedjii");
        profile.setTitleFilter("Feedjii");
        profile.setDescriptionFilter("Feedjii");
        profiles.add(profile);

        // JavaFX
        profile = new Profile("", "JavaFX");
        profile.setTitleFilter("JavaFX");
        profile.setDescriptionFilter("JavaFX");
        profiles.add(profile);

    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setActiveProfile(Profile activeProfile) {
        this.activeProfile = activeProfile;
    }

    public Profile getActiveProfile() {
        return activeProfile;
    }

    /**
     * Contains filter information related to a profile.
     */
    public static class Profile implements Comparable, Serializable {

        private String profileImage;
        private String profileName;
        private String titleFilter;
        private String descriptionFilter;
        private String link;
        private Date startDate;
        private Date endDate;
        private long id;

        /**
         * Creates a new profile.
         * 
         * @param profileImage image associated with this profile.
         * @param profileName name of this profile.
         */
        public Profile(String profileImage, String profileName) {
            id = (long) (Math.random() * Long.MAX_VALUE);
            this.profileImage = profileImage;
            this.profileName = profileName;
        }

        public void setProfileName(String name) {
            profileName = name;
        }

        public String getProfileName() {
            return profileName;
        }

        public String getImage() {
            return profileImage;
        }

        public String getDescriptionFilter() {
            return descriptionFilter;
        }

        public void setDescriptionFilter(String descriptionFilter) {
            this.descriptionFilter = descriptionFilter;
        }

        public Date getEndDateFilter() {
            return endDate;
        }

        public void setEndDateFilter(Date endDate) {
            this.endDate = endDate;
        }

        public String getLinkFilter() {
            return link;
        }

        public void setLinkFilter(String link) {
            this.link = link;
        }

        public Date getStartDateFilter() {
            return startDate;
        }

        public void setStartDateFilter(Date startDate) {
            this.startDate = startDate;
        }

        public String getTitleFilter() {
            return titleFilter;
        }

        public void setTitleFilter(String titleFilter) {
            this.titleFilter = titleFilter;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof Profile) {
                Profile obj = (Profile) o;
                return obj.getProfileName().compareTo(getProfileName());
            } else {
                return 0;
            }

        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Profile other = (Profile) obj;
            if (this.id != other.id) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return (profileName != null && !profileName.isEmpty()) ? profileName : "New profile";
        }
    }
}
