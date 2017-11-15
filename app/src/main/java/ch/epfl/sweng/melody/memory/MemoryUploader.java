package ch.epfl.sweng.melody.memory;

import java.util.Date;
import java.util.List;

import ch.epfl.sweng.melody.user.User;

class MemoryUploader {
    final private String id;
    final private String authorId;
    final private Date time;
    final private String location;
    final private String text;
    final private List<Comment> comments;
    final private List<User> likes;
    final private Memory.Privacy privacy;
    final private Boolean reminder;
    final private String photoUrl;
    final private String videoUrl;
    final private String audioUrl;
    private Memory.MemoryType memoryType;

    MemoryUploader(Memory memory) {
        id = memory.getId();
        authorId = memory.getAuthorId();
        time = memory.getTime();
        location = memory.getLocation();
        text = memory.getText();
        comments = memory.getComments();
        privacy = memory.getPrivacy();
        reminder = memory.getReminder();
        photoUrl = memory.getPhotoUrl();
        videoUrl = memory.getVideoUrl();
        audioUrl = memory.getAudioUrl();
        memoryType = memory.getMemoryType();
        likes = memory.getLikes();
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

    public String getPhotoUrl() {
        return photoUrl;
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

    public Memory.MemoryType getMemoryType() {
        return memoryType;
    }

}
