package space.collabify.android.models;

import android.location.Location;

import space.collabify.android.collabify.models.domain.UserSettings;

/**
 * This file was born on March 20, at 19:46
 */
public class User {
    private Role mRole;
    private String mName;
    private String mId;
    private Location mLocation;
    private boolean isPremium;
    private String mAccessToken;
    private UserSettings mSettings;

    public User(String name, String id, String role) {
      mName = name;
      mId = id;
      mRole = new Role();
      mRole.setRole(role);
        mSettings = new UserSettings();
        mSettings.setShowName(true);
    }

    public User(String name, String id) {
      mName = name;
      mId = id;
      mRole = new Role();
        mSettings = new UserSettings();
        mSettings.setShowName(true);
    }

    public Role getRole() {
        return mRole;
    }   //TODO: use this for settings

    public void setRole(String mRole) {
        this.mRole.setRole(mRole);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public boolean isPremium() {
      return isPremium;
    }

    public void setPremium(boolean p) {
      this.isPremium = p;
    }

    public void setAccessToken(String accessToken) {
        this.mAccessToken = accessToken;
    }

    public String getAccessToken() {
        return mAccessToken;
    }
    public UserSettings getSettings() {
        return mSettings;
    }

    public void setSettings(UserSettings settings) {
        this.mSettings.setShowName(settings.isShowName());
    }
}
