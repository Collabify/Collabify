package space.collabify.android.collabify.models.domain;

/**
 * Created by ricardolopez on 4/10/15.
 */
public class EventSettings {
    private String password;
    private boolean locationRestricted;
    private boolean allowVoting;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocationRestricted() {
        return locationRestricted;
    }

    public void setLocationRestricted(boolean locationRestricted) {
        this.locationRestricted = locationRestricted;
    }

    public boolean isAllowVoting() {
        return allowVoting;
    }

    public void setAllowVoting(boolean allowVoting) {
        this.allowVoting = allowVoting;
    }
}
