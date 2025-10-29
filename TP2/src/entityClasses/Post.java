package entityClasses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private int postId;
    private String author;
    private String content;
    private LocalDateTime timestamp;
    private List<Reply> replies;

    public Post(int postId, String author, String content) {
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.replies = new ArrayList<>();
    }

    // Getters
    public int getPostId() { return postId; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<Reply> getReplies() { return replies; }

    // Setters
    public void setContent(String content) { this.content = content; }

    // Add reply
    public void addReply(Reply reply) { replies.add(reply); }

    @Override
    public String toString() {
        return "Post by " + author + " [" + timestamp + "]: " + content;
    }
}