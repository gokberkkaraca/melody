package ch.epfl.sweng.melody.memory;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.UUID;

/**
 * Created by yusiz on 2017/10/6.
 */

public class Memory {
    private final UUID id;
    private final UUID author;
    private final Date time;
    private final String location;
    private String text;
    private FileType fileType;
    private List<File> videos;
    private List<File> photos;
    private List<File> audios;
    private List<Comment> comments;
    private Privacy privacy;
    private Boolean reminder;

    public static final int MAX_FILES = 9;
    public static final String FILE_TYPE_ERROR = "You can only upload maximum 9 photos OR 1 video OR 1 audio";


    public Memory(UUID id, UUID author, List<File> f, String t) throws Exception {
        this.id = UUID.randomUUID();
        this.author = author;
        this.time = Calendar.getInstance().getTime();
        this.location = "Lausanne";
        this.text = t;
        this.fileType = findFileType(f);
        this.videos = null;
        this.audios = null;
        this.photos = null;
        if (fileType.equals(FileType.Photo)){
            this.photos = f;
        }else if (fileType.equals(FileType.Video)){
            this.videos = f;
        }else if (fileType.equals(FileType.Audio)){
            this.audios = f;
        }
        this.comments = null;
        this.privacy = Privacy.Public;
        reminder = true;
    }

    public UUID getID() {
        return id;
    }

    public UUID getAuthor() {
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

    public enum FileType {Null, Photo, Video, Audio}

    public enum Privacy {Private, Shared, Public}

    public void editText (String s){
        this.text = s;
    }

    public void addComment (Comment c){
        this.comments.add(c);
    }

    public void deleteComment (Comment comment){
        for (int i=0; i<comments.size(); i++){
            if (comments.get(i).equals(comment) ){
               comments.remove(i);
            }
        }
    }

    public void editPrivacy (Privacy p){
        this.privacy = p;
    }

    public void editReminder (boolean b){
        this.reminder = b;
    }

    private final String[] photoExtensions = new String[]{
            "jpg", "png", "bmp", "gif", "jpeg"
    };
    private final String[] videoExtensions = new String[]{
            "avi", "flv", "flash", "mov", "wmv", "rmvb", "rm", "mp4", "mid", "3gp"
    };
    private final String[] audioExtensions = new String[]{
            "amr", "wav", "awb"
    };

    public FileType findFileType(List<File> files) throws Exception {
        int n = files.size();
        if (files.isEmpty() || files.get(0) == null || !files.get(0).exists()) {
            return FileType.Null;
        }
        if (isPhoto(files.get(0))) {
            if (n > 9) {
                throw new Exception(FILE_TYPE_ERROR);
            } else {
                boolean allPhotos = true;
                for (File f : files) {
                    if (!isPhoto(f)) {
                        allPhotos = false;
                    }
                }
                if (allPhotos = true) {
                    return FileType.Photo;
                } else {
                    throw new Exception(FILE_TYPE_ERROR);
                }
            }
        }
        if (isVideo(files.get(0))) {
            return FileType.Video;
        }
        if (isAudio(files.get(0))) {
            return FileType.Audio;
        }
        return FileType.Null;
    }

    public boolean isPhoto(File file) {
        for (String extension : photoExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVideo(File file) {
        for (String extension : videoExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAudio(File file) {
        for (String extension : audioExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }




}
