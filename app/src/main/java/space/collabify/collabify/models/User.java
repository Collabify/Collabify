package space.collabify.collabify.models;

import android.location.Location;

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

    public User(String name, String id, String role) {
      mName = name;
      mId = id;
      mRole = new Role();
      mRole.setRole(role);
    }

    public User(String name, String id) {
      mName = name;
      mId = id;
      mRole = new Role();
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
}
