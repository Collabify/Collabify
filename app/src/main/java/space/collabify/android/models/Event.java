package space.collabify.android.models;

import java.util.ArrayList;
import java.util.List;

/**
 * This file was born on March 20, at 19:42
 */
public class Event {
    private String mName;
    private boolean mAllowVoting;
    private String mLatitude;
    private String mLongitude;
    private String mPassword;
    private String mId;
    private List<User> mUserList;

    //just for server manager getEvents stub
    public Event(String name, String id, String password, boolean allowVoting) {
        this.mName = name;
        this.mId = id;
        this.mPassword = password;
        this.mAllowVoting = allowVoting;
        this.mUserList = new ArrayList<>();
    }

    public String getName() {
        return mName;
    }

    public void setName(String nName) {
        this.mName = nName;
    }

    public boolean isProtectedEvent() {
        return (mPassword != null && !("".equals(mPassword)));
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password){
        mPassword = password;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public boolean isAllowVoting() {
      return mAllowVoting;
    }

    public void setAllowVoting(boolean allowVoting) {
      mAllowVoting = allowVoting;
    }

    public List<User> getmUserList() {
        return mUserList;
    }

    public void setmUserList(List<User> mUserList) {
        this.mUserList = mUserList;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }
}
