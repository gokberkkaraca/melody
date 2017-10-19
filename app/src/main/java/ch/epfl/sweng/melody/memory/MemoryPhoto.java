package ch.epfl.sweng.melody.memory;

import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryPhoto extends Memory {
    private List<String> photosURL;

    public MemoryPhoto(UUID id, UUID author, String text, List<String> photosURL) {
        super(id, author, text);
        this.photosURL = photosURL;
    }

    public List<String> getPhotosURL (){
        return photosURL;
    }
}
