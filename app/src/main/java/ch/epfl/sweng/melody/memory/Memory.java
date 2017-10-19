package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Memory {

    private enum Privacy {Private, Shared, Public}

    private final String id;
    private final String author;
    private final Date time;
    private final String location;
    private String text;
    private List<Comment> comments;
    private Privacy privacy;
    private Boolean reminder;

    public Memory(String author, String text, String location) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.time = Calendar.getInstance().getTime();
        this.location = location;
        this.text = text;
        this.comments = null;
        this.privacy = Privacy.Public;
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
}
