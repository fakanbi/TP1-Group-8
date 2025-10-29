package database;

import entityClasses.Reply;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReplyManager {

    // Singleton instance
    private static ReplyManager instance;

    // In-memory list of replies
    private List<Reply> allReplies;

    // Private constructor (singleton)
    private ReplyManager() {
        this.allReplies = new ArrayList<>();
    }

    // Access point for the single shared instance
    public static ReplyManager getInstance() {
        if (instance == null) {
            instance = new ReplyManager();
        }
        return instance;
    }

    // CRUD operations
    public void addReply(Reply reply) { allReplies.add(reply); }

    public void deleteReply(int replyId) { allReplies.removeIf(r -> r.getReplyId() == replyId); }

    public List<Reply> getAllReplies() { return allReplies; }

    // Get replies for a specific post
    public List<Reply> getRepliesForPost(int postId) {
        return allReplies.stream()
                .filter(r -> r.getPostId() == postId)
                .collect(Collectors.toList());
    }

    // Search replies by keyword
    public List<Reply> searchReplies(String keyword) {
        if (keyword == null || keyword.isEmpty()) return new ArrayList<>(allReplies);
        return allReplies.stream()
                .filter(r -> r.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}