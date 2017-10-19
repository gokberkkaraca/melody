package ch.epfl.sweng.melody.memory;

import java.net.URL;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryVideo extends Memory {
    private URL video;

    public MemoryVideo(String author, String text, String location, URL video) {
        super(author, text, location);
        this.video = video;
    }

    public URL getVideos() {
        return video;
    }

}
