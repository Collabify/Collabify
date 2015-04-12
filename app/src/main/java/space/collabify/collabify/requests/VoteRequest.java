package space.collabify.collabify.requests;

import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;

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
