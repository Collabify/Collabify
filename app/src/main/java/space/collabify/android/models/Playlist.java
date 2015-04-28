package space.collabify.android.models;

import java.util.ArrayList;

/**
 * This file was born on March 20, at 19:46
 */
public class Playlist {
    private String mName;
    private String mId;
    private String mArtUrl;
    private ArrayList<Song> mList;
    private String mOwner;

    public Playlist(String mName, String mId, String mArtUrl, ArrayList<Song> mList) {
        this.mName = mName;
        this.mId = mId;
        this.mArtUrl = mArtUrl;
        this.mList = mList;
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

    public String getOwner() {
      return mOwner;
    }

    public void setOwner(String mOwner) {
      this.mOwner = mOwner;
    }

    public String getArtUrl(){
        return mArtUrl;
    }

    public void setArtUrl(String mArtUrl){
        this.mArtUrl = mArtUrl;
    }

    public ArrayList<Song> getmList() {
        return mList;
    }

    public void addSong(Song s) {
        mList.add(s);
    }

    public void removeSong(Song s) {
        mList.remove(s);
    }

    public void moveSong(Song s, int i) {
        // TODO: something here
    }

    public Song getCurrent() {
        try {
            return mList.get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Song getNext() {
        try {
            return mList.get(1);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public void update() {
        // TODO: something here
    }

}
