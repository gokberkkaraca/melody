package ch.epfl.sweng.melody.memory;

import java.net.URL;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryAudio extends Memory {
    private URL audio;

    public MemoryAudio(UUID author, String text, String location, URL audio) {
        super(author, text, location);
        this.audio = audio;
    }

    public URL getAudio(){
        return audio;
    }
}
