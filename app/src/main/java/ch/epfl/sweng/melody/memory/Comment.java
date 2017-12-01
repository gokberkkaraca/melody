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
        if (userContactInfo != null)
            return userContactInfo;
        else
            return new UserContactInfo("SampleUser1", "Sample User", "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/resources%2F1511445418787.jpg?alt=media&token=79ef569d-b65a-47b6-b1b9-3b32098153ff", "sample@gmail.com");
    }

    public String getContent() {
        return content;
    }

    public Date getTime() {
        return time;
    }


}
