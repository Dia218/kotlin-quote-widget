package model;

import java.util.Objects;

public class Quote {
    private final int id;
    private String author;
    private String content;
    
    public Quote(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }
    
    public int getId() {
        return id;
    }
    
    public void update(String newAuthor, String newContent) {
        this.author = newAuthor;
        this.content = newContent;
    }
    
    public String[] getContentAndAuthor() {
        return new String[]{this.content, this.author};
    }
    
    public String getInfo() {
        return id + " / " + author + " / " + content;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Quote quote = (Quote) obj;
        return id == quote.id &&
                Objects.equals(author, quote.author) &&
                Objects.equals(content, quote.content);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, author, content);
    }
}
