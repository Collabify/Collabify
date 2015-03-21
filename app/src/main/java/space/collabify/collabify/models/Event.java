package space.collabify.collabify.models;

import java.util.List;

/**
 * This file was born on March 20, at 19:42
 */
public class Event {
    private String mName;
    private boolean mPrivateEvent;
    private boolean mProtectedEvent;
    private String mPassword;
    private int mId;
    private List<User> mUserList;

    //just for server manager getEvents stub
    public Event(String name, boolean passwordProtected, String password){
        this.mName = name;
        this.mProtectedEvent = passwordProtected;
        this.mPassword = password;
    }

    public String getName() {
        return mName;
    }

    public void setName(String nName) {
        this.mName = nName;
    }

    public boolean isPrivateEvent() {
        return mPrivateEvent;
    }

    public void setPrivateEvent(boolean mPrivateEvent) {
        this.mPrivateEvent = mPrivateEvent;
    }

    public boolean isProtectedEvent() {
        return mProtectedEvent;
    }

    public void setProtectedEvent(boolean mProtectedEvent) {
        this.mProtectedEvent = mProtectedEvent;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password){
        mPassword = password;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public List<User> getmUserList() {
        return mUserList;
    }

    public void setmUserList(List<User> mUserList) {
        this.mUserList = mUserList;
    }
}
