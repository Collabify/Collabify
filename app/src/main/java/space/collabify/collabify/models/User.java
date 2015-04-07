package space.collabify.collabify.models;

import android.location.Location;

/**
 * This file was born on March 20, at 19:46
 */
public class User {
    private IRole mRole;
    private String mName;
    private int mId;
    private Location mLocation;

    public IRole getRole() {
        return mRole;
    }   //TODO: use this for settings

    public void setRole(IRole mRole) {
        this.mRole = mRole;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }
}
