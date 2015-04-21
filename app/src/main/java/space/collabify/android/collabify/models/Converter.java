package space.collabify.android.collabify.models;

import java.util.ArrayList;
import java.util.List;

import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Location;
import space.collabify.android.collabify.models.network.EventDO;
import space.collabify.android.collabify.models.network.EventRequestDO;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.models.Event;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;

/**
 * This file was born on April 12, at 21:20
 * Meant to contain functions to convert the domain/network models to the app models.
 * Fill in methods as needed
 */
public class Converter {
    public static Playlist getAppPlaylist(space.collabify.android.collabify.models.domain.Playlist playlist){
        ArrayList<Song> appSongs = new ArrayList<>(playlist.getSongs().size());

        for(space.collabify.android.collabify.models.domain.Song song: playlist.getSongs()){
            appSongs.add(getAppSong(song));
        }

        Playlist appPlaylist = new Playlist("converted name", -1, appSongs);
        return appPlaylist;
    }

    public static Song getAppSong(space.collabify.android.collabify.models.domain.Song song){
        Song appSong = new Song(song.getTitle(),song.getArtist(), song.getAlbum(),
                song.getYear(), song.getSongId(), song.getArtworkUrl(), song.getUserId());
        return appSong;
    }

    public static Event getAppEvent(space.collabify.android.collabify.models.domain.Event event) {
      Event appEvent = new Event(event.getName(), event.getEventId(),
        event.getSettings().getPassword(), event.getSettings().isAllowVoting());
      return appEvent;
    }

    public static EventRequestDO makeEvent(Event event) {
      EventRequestDO eventNew = new EventRequestDO();
      eventNew.setName(event.getName());

      EventSettings eventSettings = new EventSettings();
      eventSettings.setPassword(event.getPassword());
      eventSettings.setAllowVoting(event.isAllowVoting());
      eventNew.setSettings(eventSettings);

      Location eventLocation = new Location();
      eventLocation.setLatitude("296");
      eventLocation.setLongitude("-248");
      eventNew.setLocation(eventLocation);

      return eventNew;
    }

    public static List<Event> toEventList(List<EventDO> eventDOs) {
        List<Event> events = new ArrayList<Event>();
        if (eventDOs != null) {
            for (EventDO eventDO : eventDOs) {
                Event ev = new Event(eventDO.getName(), eventDO.getEventId(), eventDO.getSettings().getPassword(),
                        eventDO.getSettings().isAllowVoting());

                events.add(ev);
            }
        }
        return events;
    }

    public static List<User> toUserList(List<UserDO> userDOs) {
        List<User> users = new ArrayList<User>();

        if (userDOs != null) {
            for (UserDO userDO : userDOs) {
                User u = new User(userDO.getName(), userDO.getUserId(), userDO.getRole());
                users.add(u);
            }
        }

        return users;
    }
}
