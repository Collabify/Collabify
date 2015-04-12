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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public EventSettings getSettings() {
        return settings;
    }

    public void setSettings(EventSettings settings) {
        this.settings = settings;
    }
}
