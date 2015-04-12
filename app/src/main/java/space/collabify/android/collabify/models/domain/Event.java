package space.collabify.android.collabify.models.domain;

import java.util.List;

/**
 * Created by ricardolopez on 4/10/15.
 */
public class Event {
    private String name;
    private String eventId;
    private List<String> userIds;
    private Location location;
    private Playlist playlist;
    private EventSettings settings;
}
