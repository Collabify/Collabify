package space.collabify.android.collabify.models.domain;

/**
 * Created by andrew on 4/24/15.
 */
public class Vote {
  private boolean isUpvoted;
  private boolean isDownvoted;

  public boolean isUpvoted() {
    return isUpvoted;
  }

  public void setUpvoted(boolean isUpvoted) {
    this.isUpvoted = isUpvoted;
  }

  public boolean isDownvoted() {
    return isDownvoted;
  }

  public void setDownvoted(boolean isDownvoted) {
    this.isDownvoted = isDownvoted;
  }
}
