package space.collabify.collabify.models;

/**
 * This file was born on March 20, at 19:46
 */
public class Song {
  private String mTitle;
  private String mArtist;
  private String mAlbum;
  private int mYear;
  private int mId;

  // TODO: Probably want to cache artwork somehow
  private String mArtwork;

  public Song(String mTitle, String mArtist, String mAlbum, int mYear, int mId, String mArtwork) {
    this.mTitle = mTitle;
    this.mArtist = mArtist;
    this.mAlbum = mAlbum;
    this.mYear = mYear;
    this.mId = mId;
    this.mArtwork = mArtwork;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String mTitle) {
    this.mTitle = mTitle;
  }

  public String getArtist() {
    return mArtist;
  }

  public void setArtist(String mArtist) {
    this.mArtist = mArtist;
  }

  public String getAlbum() {
    return mAlbum;
  }

  public void setAlbum(String mAlbum) {
    this.mAlbum = mAlbum;
  }

  public int getYear() {
    return mYear;
  }

  public void setYear(int mYear) {
    this.mYear = mYear;
  }

  public int getId() {
    return mId;
  }

  public void setId(int mId) {
    this.mId = mId;
  }

  public String getArtwork() {
    return mArtwork;
  }

  public void setArtwork(String mArtwork) {
    this.mArtwork = mArtwork;
  }

  public void upvote() {
    // TODO: something here
  }

  public void downvote() {
    // TODO: something here
  }

  public String getLyrics() {
    // TODO: something here
    return "I found some lyrics!\nYou should fix me though ;)";
  }

}
