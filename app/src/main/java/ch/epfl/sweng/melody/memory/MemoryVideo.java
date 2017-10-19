package ch.epfl.sweng.melody.memory;

import java.net.URL;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryVideo extends Memory {
    private URL video;

    public MemoryVideo(UUID id, UUID author, String text, URL video) {
        super(id, author, text);
        this.video = video;
    }

    public URL getVideos(){
        return video;
    }

}
