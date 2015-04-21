package space.collabify.android.collabify.models.network;

import space.collabify.android.collabify.models.domain.UserSettings;

/**
 * Created by ricardolopez on 4/16/15.
 */
public class UserRequestDO {
    private String name;
    private UserSettings settings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }
}
