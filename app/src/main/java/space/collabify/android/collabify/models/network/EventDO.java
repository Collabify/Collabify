package space.collabify.android.collabify.models.network;

import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Location;

/**
 * Definition of the Event object returned from the Collabify API defined in CollabifyService.java
 *
 * Created by ricardolopez on 4/11/15.
 */
public class EventDO {
    private String name;
    private String eventId;
    private Location location;
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
