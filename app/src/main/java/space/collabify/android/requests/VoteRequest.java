package space.collabify.android.requests;

import space.collabify.android.models.Song;

/**
 * This file was born on April 11, at 20:15
 */
public class VoteRequest {
    //types of vote requests
    public enum VoteType {
        UPVOTE, DOWNVOTE, CLEAR_VOTE
    }

    public VoteType voteType;
    public Song song;


    public VoteRequest(Song song, VoteType voteType) {
        this.song = song;
        this.voteType = voteType;
    }
}
