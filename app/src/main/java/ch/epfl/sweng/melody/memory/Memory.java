package ch.epfl.sweng.melody.memory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.melody.user.User;


public class Memory {
    private String id;
    private String authorId;
    private Date time;
    private String location;
    private String text;
    private List<Comment> comments;
    private List<User> likes;
    private Privacy privacy;
    private MemoryType memoryType;
    private Boolean reminder;
    private String photoUrl;
    private String videoUrl;
    private String audioUrl;

    private Memory(MemoryBuilder memoryBuilder) {
        this.id = memoryBuilder.id;
        this.time = memoryBuilder.time;
        this.authorId = memoryBuilder.authorId;
        this.text = memoryBuilder.text;
        this.location = memoryBuilder.location;
        this.privacy = memoryBuilder.privacy;
        this.reminder = memoryBuilder.reminder;

        this.comments = memoryBuilder.comments;
        this.likes = memoryBuilder.likes;
        this.photoUrl = memoryBuilder.photoUrl;
        this.videoUrl = memoryBuilder.videoUrl;

        this.audioUrl = memoryBuilder.audioUrl;
        this.memoryType = memoryBuilder.memoryType;
    }

    public Memory() {
        comments = new ArrayList<>();
        likes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public Date getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getText() {
        return text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Privacy getPrivacy() {
        return privacy;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }

    public Memory getMemory() {
        return this;
    }

    public MemoryUploader upload() {
        return new MemoryUploader(this);
    }

    public void likeAction(User user){
        if (likes.contains(user)){
            likes.remove(user);
        }
        else {
            likes.add(user);
        }
    }

    public int getLikeNumber(){
        return likes.size();
    }

    public enum Privacy {PRIVATE, SHARED, PUBLIC}

    public enum MemoryType {TEXT, PHOTO, VIDEO, AUDIO}

    public static class MemoryBuilder {
        private final String id;
        private final String authorId;
        private final Date time;
        private final String location;
        private final String text;
        private final Privacy privacy;
        private final Boolean reminder;
        private final Long MAX_ID = Long.MAX_VALUE;
        private List<Comment> comments;
        private List<User> likes;
        private MemoryType memoryType;
        private String photoUrl;
        private String videoUrl;
        private String audioUrl;

        public MemoryBuilder(String authorId, String text, String location) {
            this.id = Long.toString(MAX_ID - System.currentTimeMillis());
            this.time = Calendar.getInstance().getTime();
            this.authorId = authorId;
            this.text = text;
            this.location = location;
            this.privacy = Privacy.PUBLIC;
            this.reminder = true;
            this.memoryType = MemoryType.TEXT;
            this.comments = new ArrayList<>();
            this.likes = new ArrayList<>();
        }

        public MemoryBuilder photo(String photoUrl) {
            this.photoUrl = photoUrl;
            this.memoryType = MemoryType.PHOTO;
            return this;
        }

        public MemoryBuilder video(String videoUrl) {
            this.videoUrl = videoUrl;
            this.memoryType = MemoryType.VIDEO;
            return this;
        }

        public MemoryBuilder audio(String audioUrl) {
            this.audioUrl = audioUrl;
            this.memoryType = MemoryType.AUDIO;
            return this;
        }

        public MemoryBuilder comments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public MemoryBuilder likes(List<User> likes){
            this.likes = likes;
            return this;
        }

        public Memory build() {
            return new Memory(this);
        }
    }
}
