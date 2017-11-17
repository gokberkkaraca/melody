package ch.epfl.sweng.melody.memory;


public class Tag {
    private String id;
    private String content;

    public Tag(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() { return this.content; }
}
