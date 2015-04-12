package space.collabify.android.collabify.models.network;

import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Location;

/**
 * Use this class to send requests to the Collabify API defined CollabifyService.java
 *
 * Created by ricardolopez on 4/11/15.
 */
public class EventRequestDO {
    private String name;
    private Location location;
    private EventSettings settings;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EventSettings getSettings() {
        return settings;
    }

    public void setSettings(EventSettings settings) {
        this.settings = settings;
    }
}
