package entityClasses;

import java.time.LocalDateTime;

public class Reply {
    private int replyId;
    private int postId;
    private String author;
    private String content;
    private LocalDateTime timestamp;

    public Reply(int replyId, int postId, String author, String content) {
        this.replyId = replyId;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public int getReplyId() { return replyId; }
    public int getPostId() { return postId; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Reply by " + author + " [" + timestamp + "]: " + content;
    }
}