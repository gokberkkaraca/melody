package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;

public class Comment {

    private String memoryId;
    private String authorId;
    private String content;
    private Date time;
    private String id;

    public Comment(String memoryId, String authorId, String content) {
        this.id = Long.toString(System.currentTimeMillis());
        this.memoryId = memoryId;
        this.authorId = authorId;
        this.content = content;
        this.time = Calendar.getInstance().getTime();
    }

    public Comment() {}

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
