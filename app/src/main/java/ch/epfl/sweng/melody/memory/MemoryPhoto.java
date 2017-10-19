package ch.epfl.sweng.melody.memory;

import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryPhoto extends Memory {
    private List<String> photos;

    public MemoryPhoto(){

    }

    public MemoryPhoto(UUID author, String text, String location, List<String> photos) {
        super(author, text, location);
        this.photos = photos;
    }

    public List<String> getPhotos() {
        return photos;
    }
}
