public class PostFeedActivity extends AppCompatActivity {
    private RecyclerView postsRecyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feed);

        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize sample posts
        postList = new ArrayList<>();
        postList.add(new Post("User1", "2h ago", "This is my first post! So excited to be here!", R.drawable.profile1, R.drawable.post1));
        postList.add(new Post("Traveler", "5h ago", "Beautiful sunset at the beach today! #vacation", R.drawable.profile2, R.drawable.post2));
        postList.add(new Post("FoodLover", "1d ago", "Homemade pizza night! What's your favorite topping?", R.drawable.profile3, R.drawable.post3));

        postAdapter = new PostAdapter(postList);
        postsRecyclerView.setAdapter(postAdapter);

        FloatingActionButton addPostButton = findViewById(R.id.addPostButton);
        addPostButton.setOnClickListener(v -> {
            // Handle add post button click
            Toast.makeText(this, "Add new post", Toast.LENGTH_SHORT).show();
        });
    }
}