package ch.epfl.sweng.melody.memory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.user.User;


public class Memory {
    public enum Privacy {PRIVATE, SHARED, PUBLIC}
    public enum MemoryType {TEXT, PHOTO, VIDEO, AUDIO}

    private String id;
    private User user;
    private Date time;
    private SerializableLocation serializableLocation;
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
        this.user = memoryBuilder.user;
        this.text = memoryBuilder.text;
        this.serializableLocation = memoryBuilder.serializableLocation;
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

    public Long getLongId() {
        long longId = 0L;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());
        }
        return longId;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Date getTime() {
        return time;
    }

    public SerializableLocation getSerializableLocation() {
        return serializableLocation;
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

    public void likeAction(User user) {
        for (User liker: likes) {
            if (liker.getId().equals(user.getId())){
                likes.remove(liker);
                updateMemory();
                return;
            }
        }

        likes.add(user);
        updateMemory();
    }

    boolean isLikedByUser(User user) {
        for (User liker: likes) {
            if (liker.getId().equals(user.getId())){
                return true;
            }
        }

        return false;
    }

    public int getLikeNumber() {
        return likes.size();
    }

    public int getCommentNumber() {
        return comments == null ? 0 : comments.size();
    }

    List<User> getLikes() {
        return likes;
    }

    public void updateMemory() {
        DatabaseHandler.uploadMemory(this);
    }

    public static class MemoryBuilder {
        private final String id;
        private final User user;
        private final Date time;
        private final SerializableLocation serializableLocation;
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

        public MemoryBuilder(User user, String text, SerializableLocation serializableLocation) {
            this.id = Long.toString(MAX_ID - System.currentTimeMillis());
            this.time = Calendar.getInstance().getTime();
            this.user = user;
            this.text = text;
            this.serializableLocation = serializableLocation;
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

        public MemoryBuilder likes(List<User> likes) {
            this.likes = likes;
            return this;
        }

        public Memory build() {
            return new Memory(this);
        }
    }
}
