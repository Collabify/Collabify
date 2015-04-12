package space.collabify.android.collabify.models.network;

import space.collabify.android.collabify.models.domain.UserSettings;

/**
 * Created by ricardolopez on 4/10/15.
 */
public class UserDO {
    private String name;
    private String userId;
    private UserSettings settings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }
}
