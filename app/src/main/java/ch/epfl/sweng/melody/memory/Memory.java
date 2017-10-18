package ch.epfl.sweng.melody.memory;

import android.location.Address;
import android.location.Geocoder;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/6.
 */

public class Memory {
    private final UUID id;
    private final UUID author;
    private final Date time;
    private final String location;
    private String text;
    private List<Comment> comments;
    private Privacy privacy;
    private Boolean reminder;

    Geocoder geocoder;

    public Memory(UUID id, UUID author, String text) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.time = Calendar.getInstance().getTime();
        this.location = "Lausanne";///////////////////////////////////////////////////////
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

    private enum Privacy {Private, Shared, Public}

    public void editText (String s){
        this.text = s;
    }

    public void addComment (Comment c){
        this.comments.add(c);
    }

    public void deleteComment (Comment comment){
        for (int i=0; i<comments.size(); i++){
            if (comments.get(i).equals(comment) ){
               comments.remove(i);
            }
        }
    }

    public void setPrivacy (Privacy p){
        this.privacy = p;
    }

    public void setReminder (boolean b){
        this.reminder = b;
    }

    geocoder = new Geocoder (context);

    List<Address> addresses = null;
            try {
        addresses = geocoder.getFromLocation(lat, lon, 1);
    } catch (IOException e) {

        e.printStackTrace();
    }

}
