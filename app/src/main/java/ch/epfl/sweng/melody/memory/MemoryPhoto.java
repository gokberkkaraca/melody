package ch.epfl.sweng.melody.memory;

import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryPhoto extends Memory {
    private List<String> photos;

    // public MemoryPhoto(){}

    public MemoryPhoto(String author, String text, String location, List<String> photos) {
        super(author, text, location);
        this.photos = photos;
        this.type = Type.PHOTO;
    }

    public List<String> getPhotos() {
        return photos;
    }

}
