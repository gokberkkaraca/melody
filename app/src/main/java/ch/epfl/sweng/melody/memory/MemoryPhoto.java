package ch.epfl.sweng.melody.memory;

import java.net.URL;
import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryPhoto extends Memory {
    private List<URL> photos;

    public MemoryPhoto(UUID id, UUID author, String text, List<URL> photos) {
        super(id, author, text);
        this.photos = photos;
    }

    public List<URL> getPhotos (){
        return photos;
    }
}
