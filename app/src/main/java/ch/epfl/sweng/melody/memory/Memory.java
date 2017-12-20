package ch.epfl.sweng.melody.memory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.melody.location.SerializableLocation;
import ch.epfl.sweng.melody.user.User;


public class Memory {
    private String id;
    private User user;
    private Date time;
    private SerializableLocation serializableLocation;
    private String text;
    private Map<String, Comment> comments;
    private List<User> likes;
    private List<String> tags;
    private Privacy privacy;
    private MemoryType memoryType;
    private Boolean reminder;
    private String photoUrl;
    private String videoUrl;

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
        this.tags = memoryBuilder.tags;
        this.photoUrl = memoryBuilder.photoUrl;
        this.videoUrl = memoryBuilder.videoUrl;

        this.memoryType = memoryBuilder.memoryType;
    }

    public Memory() {
        comments = new HashMap<>();
        likes = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public Long getLongId() {
        return Long.valueOf(id);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass().equals(Memory.class) && this.id.equals(((Memory) obj).getId());
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

    public Map<String, Comment> getComments() {
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

    public MemoryType getMemoryType() {
        return memoryType;
    }

    public Memory getMemory() {
        return this;
    }

    public MemoryUploader upload() {
        return new MemoryUploader(this);
    }

    public List<User> getLikes() {
        return likes;
    }

    public List<String> getTags() {
        return tags;
    }

    public enum Privacy {PRIVATE, SHARED, PUBLIC}

    public enum MemoryType {TEXT, PHOTO, VIDEO}

    public static class MemoryBuilder {
        private final String id;
        private final User user;
        private final Date time;
        private final SerializableLocation serializableLocation;
        private final String text;
        private final Privacy privacy;
        private final Boolean reminder;
        private final Long MAX_ID = Long.MAX_VALUE;
        private Map<String, Comment> comments;
        private List<User> likes;
        private List<String> tags;
        private MemoryType memoryType;
        private String photoUrl;
        private String videoUrl;

        public MemoryBuilder(User user, String text, SerializableLocation serializableLocation, Privacy privacy) {
            this.id = Long.toString(MAX_ID - System.currentTimeMillis());
            this.time = Calendar.getInstance().getTime();
            this.user = user;
            this.text = text;
            this.serializableLocation = serializableLocation;
            if (privacy == null)
                this.privacy = Privacy.PUBLIC;
            else
                this.privacy = privacy;
            this.reminder = true;
            this.memoryType = MemoryType.TEXT;
            this.comments = new HashMap<>();
            this.likes = new ArrayList<>();
            this.tags = new ArrayList<>();
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

        public MemoryBuilder comments(Map<String, Comment> comments) {
            this.comments = comments;
            return this;
        }

        public MemoryBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public Memory build() {
            return new Memory(this);
        }
    }
}