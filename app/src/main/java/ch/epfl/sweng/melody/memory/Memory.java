package ch.epfl.sweng.melody.memory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Memory {
    public enum Privacy {PRIVATE, SHARED, Public};
    public enum Type {TEXT, PHOTO, VIDEO, AUDIO};
    private  String id;
    private  String author;
    private  Date time;
    private  String location;
    private String text;
    private List<Comment> comments;
    private Privacy privacy;
    protected Type type;
    private Boolean reminder;

    public Memory() {

    }

    public Memory(String author, String text, String location) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.time = Calendar.getInstance().getTime();
        this.location = location;
        this.text = text;
        this.comments = new ArrayList<Comment>();
        this.privacy = Privacy.Public;
        this.type = Type.TEXT;
        reminder = true;
    }

    public String getId() {
        return id;
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

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }


    public Privacy getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Privacy privacy) {
        this.privacy = privacy;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

    public Type getType() {
        return type;
    }

}
