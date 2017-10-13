package ch.epfl.sweng.melody.memory;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/6.
 */

public class Memory {
    public Privacy privacy;
    public Boolean reminder;
    private UUID id;
    private Date time;
    private String location;
    private String text;
    private List<File> videos;
    private List<File> photos;
    private List<File> audios;
    public List<String> comments;

    public Memory(String t) {
        this.id = UUID.randomUUID();
        this.time = Calendar.getInstance().getTime();
        this.location = "Lausanne";
        this.text = t;
        this.videos = null;
        this.audios = null;
        this.photos = null;
        this.privacy = Privacy.pub;
        reminder = true;
    }

    public UUID getID() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public String getLovation() {
        return location;
    }

    public String getText() {
        return text;
    }

    public enum Privacy {priv, shared, pub}
}
