package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;

import ch.epfl.sweng.melody.user.UserContactInfo;

public class Comment {

    private String memoryId;
    private UserContactInfo userContactInfo;
    private String content;
    private Date time;
    private String id;

    public Comment(String memoryId, UserContactInfo user, String content) {

        if (user == null)
            throw new IllegalArgumentException();

        this.id = Long.toString(System.currentTimeMillis());
        this.memoryId = memoryId;
        this.userContactInfo = user;
        this.content = content;
        this.time = Calendar.getInstance().getTime();
    }

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public String getMemoryId() {
        return memoryId;
    }

    public UserContactInfo getUserContactInfo() {
        return userContactInfo;
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }


}
