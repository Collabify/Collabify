package space.collabify.android.managers;

import retrofit.ResponseCallback;
import retrofit.client.Response;

/**
 * Created by ricardolopez on 4/16/15.
 */
public abstract class CollabifyResponseCallback extends ResponseCallback {
    public abstract void exception(Exception e);
}
