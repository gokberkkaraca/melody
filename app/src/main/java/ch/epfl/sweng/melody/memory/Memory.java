package ch.epfl.sweng.melody.memory;

import android.location.Geocoder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/6.
 */

public class Memory {
    private final UUID id;
    private final UUID author;
    private final Date time;
    private final String location;
    Geocoder geocoder;
    private String text;
    private List<Comment> comments;
    private Privacy privacy;
    private Boolean reminder;

    public Memory(UUID author, String text, String location) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.time = Calendar.getInstance().getTime();
        this.location = location;
        this.text = text;
        this.comments = null;
        this.privacy = Privacy.Public;
        reminder = true;
    }

    public UUID getID() {
        return id;
    }

    public UUID getAuthor() {
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

    public void editText(String s) {
        this.text = s;
    }

    public void addComment(Comment c) {
        this.comments.add(c);
    }

    public void deleteComment(Comment comment) {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).equals(comment)) {
                comments.remove(i);
            }
        }
    }

    public void setPrivacy(Privacy p) {
        this.privacy = p;
    }

    public void setReminder(boolean b) {
        this.reminder = b;
    }

    private enum Privacy {Private, Shared, Public}


}
