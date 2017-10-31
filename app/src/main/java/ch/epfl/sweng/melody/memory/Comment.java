package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

class Comment {

    private final String memoryId;
    private final String authorId;
    private final String content;
    private final Date time;
    private final String id;

    Comment(String memoryId, String authorId, String content) {
        this.id = Long.toString(System.currentTimeMillis());
        this.memoryId = memoryId;
        this.authorId = authorId;
        this.content = content;
        this.time = Calendar.getInstance().getTime();
    }

    public String getId() {
        return id;
    }

    public String getMemoryId() {
        return memoryId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }
}
