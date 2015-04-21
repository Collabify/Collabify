package space.collabify.android.collabify.models.network;

import space.collabify.android.collabify.models.domain.UserSettings;

/**
 * Definition of the Event object returned from the Collabify API defined in CollabifyService.java
 *
 * Created by ricardolopez on 4/10/15.
 */
public class UserDO {
    private String name;
    private String userId;
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
