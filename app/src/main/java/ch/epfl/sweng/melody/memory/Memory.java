package ch.epfl.sweng.melody.memory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class Memory {
    private  String id;
    private  String author;
    private  Date time;
    private  String location;
    private String text;
    private List<Comment> comments;
    private MemoryBuilder.Privacy privacy;
    private Boolean reminder;
    private List<String> photos;
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
        this.photos = memoryBuilder.photoUrl;
        this.videoUrl = memoryBuilder.videoUrl;

        this.audioUrl = memoryBuilder.audioUrl;
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


    public MemoryBuilder.Privacy getPrivacy() {
        return privacy;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public List<String> getPhotos(){
        return photos;
    }

    public String getVideoUrl(){
        return videoUrl;
    }
    public String getAudioUrl(){
        return audioUrl;
    }
    public Memory getMemory(){
        return this;
    }

    public MemoryUploader upload(){
        return new MemoryUploader(this);
    }

    public static class MemoryBuilder{
        public enum Privacy {PRIVATE, SHARED, Public};
        private  String id;
        private  String author;
        private  Date time;
        private  String location;
        private String text;
        private List<Comment> comments;
        private Privacy privacy;
        private Boolean reminder;

        private List<String> photoUrl;
        private String videoUrl;
        private String audioUrl;

        public MemoryBuilder(String author, String text, String location){
            this.id = UUID.randomUUID().toString();
            this.time = Calendar.getInstance().getTime();
            this.author = author;
            this.text = text;
            this.location = location;
            this.privacy = Privacy.Public;
            this.reminder = true;
        }
         public MemoryBuilder photos(List<String> photoUrl){
             this.photoUrl = photoUrl;
             return this;
         }

         public MemoryBuilder video(String videoUrl){
             this.videoUrl = videoUrl;
             return this;
         }
         public MemoryBuilder audio(String audioUrl){
             this.audioUrl = audioUrl;
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
