package ch.epfl.sweng.melody.memory;

import java.util.Date;
import java.util.List;

import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.user.User;

class MemoryUploader {
    final private String id;
    final private User user;
    final private Date time;
    final private SerializableLocation serializableLocation;
    final private String text;
    final private List<Comment> comments;
    final private List<User> likes;
    final private List<String> tags;
    final private Memory.Privacy privacy;
    final private Boolean reminder;
    final private String photoUrl;
    final private String videoUrl;
    final private String audioUrl;
    private Memory.MemoryType memoryType;

    MemoryUploader(Memory memory) {
        id = memory.getId();
        user = memory.getUser();
        time = memory.getTime();
        serializableLocation = memory.getSerializableLocation();
        text = memory.getText();
        comments = memory.getComments();
        privacy = memory.getPrivacy();
        reminder = memory.getReminder();
        photoUrl = memory.getPhotoUrl();
        videoUrl = memory.getVideoUrl();
        audioUrl = memory.getAudioUrl();
        memoryType = memory.getMemoryType();
        likes = memory.getLikes();
        tags = memory.getTags();
    }

    public User getUser() {
        return user;
    }

    public Date getTime() {
        return time;
    }

    public SerializableLocation getSerializableLocation() {
        return serializableLocation;
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

    public List<User> getLikes() {
        return likes;
    }

    public List<String> getTags() { return tags; }

    public Memory.MemoryType getMemoryType() {
        return memoryType;
    }

}
