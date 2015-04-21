package space.collabify.android.models;


import space.collabify.android.managers.AppManager;

/**
 * This file was born on March 20, at 19:46
 */
public class Song {
    private String mTitle;
    private String mArtist;
    private String mAlbum;
    private int mYear;
    private String mId;
    private String mUserId;


    private boolean mIsUpvoted;
    private boolean mIsDownvoted;

    // TODO: Probably want to cache artwork somehow
    private String mArtwork;

    public Song(String mTitle, String mArtist, String mAlbum, int mYear, String mId, String mArtwork, String mUserId) {
        this.mTitle = mTitle;
        this.mArtist = mArtist;
        this.mAlbum = mAlbum;
        this.mYear = mYear;
        this.mId = mId;
        this.mArtwork = mArtwork;
        this.mUserId = mUserId;
        this.mIsUpvoted = false;
        this.mIsDownvoted = false;
    }

    public boolean wasAddedByUser() {
      return (AppManager.getInstance().getUser().getId()).equals(mUserId);
    }

    public String getUserId() {
      return mUserId;
    }

    public void setUserId(String mUserId) {
      this.mUserId = mUserId;
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

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getArtwork() {
        return mArtwork;
    }

    public void setArtwork(String mArtwork) {
        this.mArtwork = mArtwork;
    }


    public boolean isDownvoted() {
        return mIsDownvoted;
    }

    public boolean isUpvoted() {
        return mIsUpvoted;
    }

    public void clearVote(){
        mIsDownvoted = false;
        mIsUpvoted = false;
    }

    public void upvote() {
        // TODO: something here
        mIsUpvoted = true;
        mIsDownvoted = false;
    }

    public void downvote() {
        // TODO: something here
        mIsUpvoted = false;
        mIsDownvoted = true;
    }

    public String getLyrics() {
        // TODO: something here
        return "I found some lyrics!\nYou should fix me though ;)";
    }

}
