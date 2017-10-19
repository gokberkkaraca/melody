package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/13.
 */

public class Comment {
    public final UUID memory;
    public final UUID author;
    public final String content;
    public final Date time;

    public Comment(UUID memory, UUID author, String content) {
        this.memory = memory;
        this.author = author;
        this.content = content;
        this.time = Calendar.getInstance().getTime();
    }

}
