package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;

public class Comment {

    private final String memory;
    private final String author;
    private final String content;
    private final Date time;

    public Comment(String memory, String author, String content) {
        this.memory = memory;
        this.author = author;
        this.content = content;
        this.time = Calendar.getInstance().getTime();
    }

    public String getMemory() {
        return memory;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }
}
