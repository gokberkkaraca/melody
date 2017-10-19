package ch.epfl.sweng.melody.memory;

import android.net.Uri;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryPhoto extends Memory {
    private String photos;

    public MemoryPhoto(UUID id, UUID author, String text, String photos) {
        super(id, author, text);
        this.photos = photos;
    }

    public String getPhotos (){
        return photos;
    }
}
