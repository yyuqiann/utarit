public class Post {
    private String username;
    private String timeAgo;
    private String content;
    private int profileImage;
    private int postImage;
    private int likeCount;

    public Post(String username, String timeAgo, String content, int profileImage, int postImage) {
        this.username = username;
        this.timeAgo = timeAgo;
        this.content = content;
        this.profileImage = profileImage;
        this.postImage = postImage;
        this.likeCount = 0;
    }

    // Add getters and setters here
    public String getUsername() { return username; }
    public String getTimeAgo() { return timeAgo; }
    public String getContent() { return content; }
    public int getProfileImage() { return profileImage; }
    public int getPostImage() { return postImage; }
    public int getLikeCount() { return likeCount; }
}