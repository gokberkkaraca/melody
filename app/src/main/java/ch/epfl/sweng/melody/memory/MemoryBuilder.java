//package ch.epfl.sweng.melody.memory;
//
//import java.net.URL;
//import java.util.List;
//import java.util.UUID;
//
//public class MemoryBuilder {
//    private UUID author;
//    private String text;
//    private List<URL> photos;
//    private URL video;
//    private URL audio;
//
//    public MemoryBuilder setAuthor(UUID author) {
//        this.author = author;
//        return this;
//    }
//
//    public MemoryBuilder setText(String text) {
//        this.text = text;
//        return this;
//    }
//
//    public MemoryBuilder setPhotos(List<URL> photos) {
//        this.photos = photos;
//        return this;
//    }
//
//    public MemoryBuilder setVideo(URL video) {
//        this.video = video;
//        return this;
//    }
//
//    public MemoryBuilder setAudio(URL audio) {
//        this.audio = audio;
//        return this;
//    }
//
//    public Memory createMemory() throws Exception {
//        return new Memory(author, text, photos, video, audio);
//    }
//}