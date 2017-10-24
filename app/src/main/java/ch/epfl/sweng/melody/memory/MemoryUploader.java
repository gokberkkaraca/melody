package ch.epfl.sweng.melody.memory;

import java.util.Date;
import java.util.List;

/**
 * Created by maxwell on 24/10/2017.
 */

public class MemoryUploader {
    private  String id;
    private  String author;
    private Date time;
    private  String location;
    private String text;
    private List<Comment> comments;
    private Memory.MemoryBuilder.Privacy privacy;
    private Boolean reminder;
    private List<String> photos;
    private String videoUrl;
    private String audioUrl;

    protected MemoryUploader(Memory memory){
        id = memory.getId();
        author = memory.getAuthor();
        time = memory.getTime();
        location = memory.getLocation();
        text = memory.getText();
        comments = memory.getComments();
        privacy = memory.getPrivacy();
        reminder = memory.getReminder();
        photos = memory.getPhotos();
        videoUrl = memory.getVideoUrl();
        audioUrl = memory.getAudioUrl();
    }

    public String getAuthor() {
        return author;
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

    public Memory.MemoryBuilder.Privacy getPrivacy() {
        return privacy;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public List<String> getPhotos() {
        return photos;
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
