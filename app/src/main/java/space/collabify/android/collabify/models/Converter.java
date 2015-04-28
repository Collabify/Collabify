package space.collabify.android.collabify.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Location;
import space.collabify.android.collabify.models.domain.Role;
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


    /** Not sure what this even does */
    public static List<Song> updatePlaylist(List<Song> original, List<space.collabify.android.collabify.models.domain.Song> updated) {
        if (updated == null) {
            return null;
        }

        List<Song> converted = new ArrayList<Song>();

        // convert the domain songs to model songs
        for (space.collabify.android.collabify.models.domain.Song song: updated) {
            converted.add(new Song(song.getTitle(),
                    song.getArtist(),
                    song.getAlbum(),
                    song.getYear(),
                    song.getSongId(),
                    song.getArtworkUrl(),
                    song.getUserId()));
        }

        // if original is not null we are creating an updated playlist
        if (original != null) {
            Map<String, Song> songMap = new HashMap<String, Song>();

            for (Song song: original) {
                if (song.isDownvoted() || song.isUpvoted()) {
                    songMap.put(song.getId(), song);
                }
            }

            for (Song song: converted) {
                Song match = songMap.get(song.getId());
                if (match != null && match.isDownvoted()) {
                    song.downvote();
                }
                else if (match != null && match.isUpvoted()) {
                    song.upvote();
                }
            }
        }

        return converted;
    }

    public static List<Song> toPlaylist(space.collabify.android.collabify.models.domain.Playlist current) {
      List<space.collabify.android.collabify.models.domain.Song> temp = current.getSongs();
      space.collabify.android.collabify.models.domain.Song currentSong = current.getCurrentSong();
      space.collabify.android.collabify.models.domain.Song nextSong = current.getNextSong();

      return toSongs(temp);
    }

    public static List<Song> toSongs(List<space.collabify.android.collabify.models.domain.Song> songs) {
        List<Song> appSongs = new ArrayList<Song>();

        for (space.collabify.android.collabify.models.domain.Song curr: songs) {
            appSongs.add(toSong(curr));
        }

        return appSongs;
    }

    public static Song toSong(space.collabify.android.collabify.models.domain.Song song){
        if (song == null) {
            return null;
        }

        Song appSong = new Song(song.getTitle(),song.getArtist(), song.getAlbum(),
                song.getYear(), song.getSongId(), song.getArtworkUrl(), song.getUserId());

        if (song.getVote().isDownvoted()) {
            appSong.downvote();
        }
        else if (song.getVote().isUpvoted()) {
            appSong.upvote();
        }
        return appSong;
    }

    public static Event getAppEvent(space.collabify.android.collabify.models.domain.Event event) {
      Event appEvent = new Event(event.getName(), event.getEventId(),
        event.getSettings().getPassword(), event.getSettings().isAllowVoting());

      appEvent.setLatitude(event.getLocation().getLatitude());
      appEvent.setLongitude(event.getLocation().getLongitude());
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

    public static Role toRole(String role) {
      Role temp = new Role();
      temp.setRole(role);
      return temp;
    }
    
    public static void updateUser(User currUser, space.collabify.android.collabify.models.domain.User newUser) {
        currUser.setRole(newUser.getRole());
    }
}
