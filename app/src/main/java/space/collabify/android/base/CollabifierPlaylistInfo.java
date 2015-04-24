package space.collabify.android.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import space.collabify.android.R;

/**
 * Created by ricardolopez on 4/23/15.
 */
public class CollabifierPlaylistInfo extends RelativeLayout {
    public CollabifierPlaylistInfo(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.collabifier_playlist_info, this);
    }
}
