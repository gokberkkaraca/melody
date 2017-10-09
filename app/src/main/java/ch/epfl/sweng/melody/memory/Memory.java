package ch.epfl.sweng.melody.memory;
import android.location.Location;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/6.
 */

public class Memory {
    private UUID id;
    private Date time;
    private Location location;
    private String text;
    private List<File> videos;
    private List<File> photos;
    private List<File> audios;
    public Enum privacy;
    public Boolean reminder = true;
    //public List<Comments> comments;


    public UUID getID () {return id;}
    public Date getTime () {return time;}
    public Location getLovation () {return location;}
    public String getText () {return text;}



}
