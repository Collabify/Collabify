package space.collabify.android.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import space.collabify.android.R;
import space.collabify.android.collabify.models.domain.Event;
import space.collabify.android.controls.ImageToggleButton;
import space.collabify.android.managers.AppManager;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;

/**
 * This file was born on April 01 at 16:34
 */
public class PlaylistListAdapter extends ArrayAdapter<Song> {
    protected User mUser;
    protected PlaylistFragment mPlaylistFragment;
    protected int position;
    protected boolean isAllowVoting = true;
    protected List<Song> songs;

    public PlaylistListAdapter(Context context, List<Song> songs, User user, PlaylistFragment fragment){
        super(context,  R.layout.playlist_collabifier_list_row, songs);
        this.songs = songs;
        this.mPlaylistFragment = fragment;
        this.mUser = user;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.playlist_collabifier_list_row, parent, false);
        Song songItem = getItem(position);
        this.position = position - 1;

        ImageView albumArt = (ImageView) customView.findViewById(R.id.playlist_collabifier_album_art);
        TextView songIdView = (TextView) customView.findViewById(R.id.playlist_row_song_id);

        TextView rowTitle = (TextView) customView.findViewById(R.id.song_row_title);
        TextView rowArtist = (TextView) customView.findViewById(R.id.song_row_artist);
        TextView rowAddedBy = (TextView) customView.findViewById(R.id.song_row_added_by);

        ImageButton deleteButton = (ImageButton) customView.findViewById(R.id.playlist_collabifier_delete_button);
        ImageToggleButton upvoteButton = (ImageToggleButton) customView.findViewById(R.id.playlist_collabifier_upvote_button);
        ImageToggleButton downvoteButton = (ImageToggleButton) customView.findViewById(R.id.playlist_collabifier_downvote_button);

        ImageButton upButton = (ImageButton) customView.findViewById(R.id.playlist_dj_up_button);
        ImageButton downButton = (ImageButton) customView.findViewById(R.id.playlist_dj_down_button);

        if (AppManager.getInstance().getUser().getRole().isDJ()) {
          upvoteButton.setVisibility(View.INVISIBLE);
          downvoteButton.setVisibility(View.INVISIBLE);
          if (position == 0) {
            upButton.setVisibility(View.INVISIBLE);
            downButton.setVisibility(View.INVISIBLE);


            // Below makes title, artist, added by wrap to end of line because of hidden icons
            RelativeLayout.LayoutParams paramsTitle = (RelativeLayout.LayoutParams) rowTitle.getLayoutParams();
            paramsTitle.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsTitle.addRule(RelativeLayout.ALIGN_PARENT_END);
            paramsTitle.addRule(RelativeLayout.LEFT_OF, 0);
            paramsTitle.addRule(RelativeLayout.START_OF, 0);
            rowTitle.setLayoutParams(paramsTitle);

            RelativeLayout.LayoutParams paramsArtist = (RelativeLayout.LayoutParams) rowArtist.getLayoutParams();
            paramsArtist.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsArtist.addRule(RelativeLayout.ALIGN_PARENT_END);
            paramsArtist.addRule(RelativeLayout.LEFT_OF, 0);
            paramsArtist.addRule(RelativeLayout.START_OF, 0);
            rowArtist.setLayoutParams(paramsArtist);

            RelativeLayout.LayoutParams paramsAddedBy = (RelativeLayout.LayoutParams) rowAddedBy.getLayoutParams();
            paramsAddedBy.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsAddedBy.addRule(RelativeLayout.ALIGN_PARENT_END);
            paramsAddedBy.addRule(RelativeLayout.LEFT_OF, 0);
            paramsAddedBy.addRule(RelativeLayout.START_OF, 0);
            rowAddedBy.setLayoutParams(paramsAddedBy);
          } else if (position == 1) {
            upButton.setImageResource(R.drawable.ic_up_arrow_grey);
          } else if (position == songs.size() - 1) {
            downButton.setImageResource(R.drawable.ic_down_arrow_grey);
          }

          if (songs.size() == 2) {
            downButton.setImageResource(R.drawable.ic_down_arrow_grey);
          }
        } else {
            upButton.setVisibility(View.INVISIBLE);
            downButton.setVisibility(View.INVISIBLE);


            Event event = AppManager.getInstance().getEvent();

            if (AppManager.getInstance().getUser().getRole().isBlacklisted()
                  || (AppManager.getInstance().getEvent().getSettings() != null
                        && !AppManager.getInstance().getEvent().getSettings().isAllowVoting())
                  || position == 0) {

                upvoteButton.setVisibility(View.INVISIBLE);
                downvoteButton.setVisibility(View.INVISIBLE);
            }
        }

        if(!"".equals(songItem.getId())){
            //use picasso to load album art
            if(songItem.getLowArtwork() != null && !songItem.getLowArtwork().isEmpty()) {
                Picasso.with(getContext()).load(songItem.getLowArtwork()).into(albumArt);
            }

            //initialize button states
            upvoteButton.setChecked(songItem.isUpvoted());
            downvoteButton.setChecked(songItem.isDownvoted());

            //add onclick listeners to the row
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlaylistFragment.onDeleteClick(v);
                }
            });

            upvoteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPlaylistFragment.onUpvoteClick(buttonView, isChecked);
                }
            });
            downvoteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mPlaylistFragment.onDownvoteClick(buttonView, isChecked);
                }
            });

            upButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                if (position != 1) {
                  mPlaylistFragment.moveSong(v, position - 1, position - 2);
                }
              }
            });

            downButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                if (position != songs.size() - 1) {
                  mPlaylistFragment.moveSong(v, position - 1, position);
                }
              }
            });

            String artist = songItem.getArtist();

            if (!artist.equals("")) {
                artist = "(" + artist + ")";
            }

            //set up the row elements
            String title = songItem.getTitle();
            title = title.substring(0, Math.min(title.length(), 30));

            rowTitle.setText(songItem.getTitle());
            rowArtist.setText(songItem.getArtist());
            rowAddedBy.setText("Added by: " + songItem.getUserName());

            songIdView.setText(songItem.getId());

            int visibility = (isDeleteVisible(songItem) && position != 0) ? View.VISIBLE : View.INVISIBLE;
            deleteButton.setVisibility(visibility);
        }
        else {
            // only entering here when no songs in playlist (upon creating event)

            //set up the row elements
            String title = songItem.getTitle();
            rowTitle.setText(title);

            deleteButton.setVisibility(View.INVISIBLE);
            upvoteButton.setVisibility(View.INVISIBLE);
            downvoteButton.setVisibility(View.INVISIBLE);
            upButton.setVisibility(View.INVISIBLE);
            downButton.setVisibility(View.INVISIBLE);
            albumArt.setVisibility(View.INVISIBLE);
        }

        return customView;
    }

    protected boolean isDeleteVisible(Song song){
        return !mUser.getRole().isBlacklisted() &&
                (song.wasAddedByUser() || mUser.getRole().isDJ() || mUser.getRole().isPromoted());
    }
}
