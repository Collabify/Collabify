package space.collabify.android.collabify.models.domain;

/**
 * The UserSettings model from the database
 *
 * Created by ricardolopez on 4/10/15.
 */
public class UserSettings {

    private boolean showName;

    public boolean isShowName() {
        return showName;
    }

    public void setShowName(boolean showName) {
        this.showName = showName;
    }
}
