package space.collabify.collabify.models;

import java.util.ArrayList;

/**
 * This file was born on March 20, at 19:46
 */
public class Playlist {
  private String mName;
  private int mId;
  private ArrayList<Song> mList;

  public Playlist(String mName, int mId, ArrayList<Song> mList) {
    this.mName = mName;
    this.mId = mId;
    this.mList = mList;
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

  public Song getNext() {
    // TODO: something here
    return new Song();
  }

  public void update() {
    // TODO: something here
  }

}
