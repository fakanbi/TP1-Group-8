package database;

import entityClasses.Post;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PostManager {

    // Singleton instance
    private static PostManager instance;

    // In-memory storage
    private List<Post> allPosts;

    // Private constructor
    private PostManager() {
        this.allPosts = new ArrayList<>();
    }

    // Access point to get the single shared instance
    public static PostManager getInstance() {
        if (instance == null) {
            instance = new PostManager();
        }
        return instance;
    }

    // CRUD operations
    public void addPost(Post post) {
        allPosts.add(post);
    }

    public void deletePost(int postId) {
        allPosts.removeIf(p -> p.getPostId() == postId);
    }

    public List<Post> getAllPosts() {
        return allPosts;
    }

    // Search subset by keyword
    public List<Post> searchPosts(String keyword) {
        if (keyword == null || keyword.isEmpty()) return new ArrayList<>(allPosts);
        return allPosts.stream()
                .filter(p -> p.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Get a specific post
    public Post getPostById(int id) {
        return allPosts.stream()
                .filter(p -> p.getPostId() == id)
                .findFirst()
                .orElse(null);
    }
}