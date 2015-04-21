package space.collabify.android.managers;

import retrofit.Callback;

/**
 * Created by ricardolopez on 4/16/15.
 */
public interface CollabifyCallback<T> extends Callback<T> {
    void exception(Exception e);
}
