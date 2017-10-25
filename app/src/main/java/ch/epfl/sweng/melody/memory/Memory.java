package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Memory {
    public enum Privacy {PRIVATE, SHARED, PUBLIC};
    public  enum MemoryType {TEXT ,PHOTO, VIEDO, AUDIO}
    private  String id;
    private  String author;
    private  Date time;
    private  String location;
    private String text;
    private List<Comment> comments;
    private Privacy privacy;
    private MemoryType memoryType;
    private Boolean reminder;
    private String photo;
    private String videoUrl;
    private String audioUrl;

    private Memory(MemoryBuilder memoryBuilder) {
        this.id = memoryBuilder.id;
        this.time = memoryBuilder.time;
        this.author = memoryBuilder.author;
        this.text = memoryBuilder.text;
        this.location = memoryBuilder.location;
        this.privacy = memoryBuilder.privacy;
        this.reminder = memoryBuilder.reminder;

        this.comments = memoryBuilder.comments;
        this.photo = memoryBuilder.photoUrl;
        this.videoUrl = memoryBuilder.videoUrl;

        this.audioUrl = memoryBuilder.audioUrl;
        this.memoryType = memoryBuilder.memoryType;
    }

    public Memory(){
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
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

    public String getPhoto(){
        return photo;
    }

    public String getVideoUrl(){
        return videoUrl;
    }
    public String getAudioUrl(){
        return audioUrl;
    }

    public MemoryType getMemoryType(){
        return memoryType;
    }
    public Memory getMemory(){
        return this;
    }

    public MemoryUploader upload(){
        return new MemoryUploader(this);
    }

    public static class MemoryBuilder{
        private  String id;
        private  String author;
        private  Date time;
        private  String location;
        private String text;
        private List<Comment> comments;
        private Privacy privacy;
        private Boolean reminder;
        private MemoryType memoryType;
        private String photoUrl;
        private String videoUrl;
        private String audioUrl;

        public MemoryBuilder(String author, String text, String location){
            this.id = UUID.randomUUID().toString();
            this.time = Calendar.getInstance().getTime();
            this.author = author;
            this.text = text;
            this.location = location;
            this.privacy = Privacy.PUBLIC;
            this.reminder = true;
            this.memoryType = MemoryType.TEXT;
        }
         public MemoryBuilder photo(String photoUrl){
             this.photoUrl = photoUrl;
             this.memoryType = MemoryType.PHOTO;
             return this;
         }

         public MemoryBuilder video(String videoUrl){
             this.videoUrl = videoUrl;
             this.memoryType = MemoryType.VIEDO;
             return this;
         }
         public MemoryBuilder audio(String audioUrl){
             this.audioUrl = audioUrl;
             this.memoryType = MemoryType.AUDIO;
             return this;
         }
         public MemoryBuilder comments(List<Comment> comments){
             this.comments = comments;
             return this;
         }

         public Memory build(){
             return new Memory(this);
         }
    }
}
