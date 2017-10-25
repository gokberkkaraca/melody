package ch.epfl.sweng.melody.memory;

import java.util.Date;
import java.util.List;

/**
 * Created by maxwell on 24/10/2017.
 */

class MemoryUploader {
    private String id;
    private String authorId;
    private Date time;
    private String location;q
    private String text;
    private List<Comment> comments;
    private Memory.Privacy privacy;
    private Memory.MemoryType memoryType;
    private Boolean reminder;
    private String photo;
    private String videoUrl;
    private String audioUrl;

    protected MemoryUploader(Memory memory) {
        id = memory.getId();
        authorId = memory.getAuthorId();
        time = memory.getTime();
        location = memory.getLocation();
        text = memory.getText();
        comments = memory.getComments();
        privacy = memory.getPrivacy();
        reminder = memory.getReminder();
        photo = memory.getPhoto();
        videoUrl = memory.getVideoUrl();
        audioUrl = memory.getAudioUrl();
    }

    public String getAuthorId() {
        return authorId;
    }

    public Date getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Memory.Privacy getPrivacy() {
        return privacy;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public String getPhoto() {
        return photo;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getId() {

        return id;
    }
}
