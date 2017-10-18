package ch.epfl.sweng.melody.memory;

import java.net.URL;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/18.
 */

public class MemoryAudio extends Memory {
    private URL audio;

    public MemoryAudio(UUID id, UUID author, String text, URL audio) {
        super(id, author, text);
        this.audio = audio;
    }

    public URL getAudio(){
        return audio;
    }
}
