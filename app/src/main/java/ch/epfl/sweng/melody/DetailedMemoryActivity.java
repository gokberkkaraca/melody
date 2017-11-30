package ch.epfl.sweng.melody;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Comment;
import ch.epfl.sweng.melody.memory.CommentAdapter;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.user.UserContactInfo;
import ch.epfl.sweng.melody.util.MenuButtons;

public class DetailedMemoryActivity extends AppCompatActivity {
    private static CommentAdapter commentAdapter;
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm aa", Locale.FRANCE);
    private Memory memory;
    private String memoryId;
    private List<Comment> commentList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memory_detail);
        memoryId = getIntent().getStringExtra("memoryId");

        TextView memoryText = findViewById(R.id.memoryText);

        RelativeLayout videoSpace = findViewById(R.id.memoryImageOrVideo);

        recyclerView = findViewById(R.id.comments_recyclerView);

        memoryText.setVisibility(View.GONE);
        videoSpace.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        fetchMemoryFromDatabase();

        LinearLayout commentsContainer = findViewById(R.id.memoryComments);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 5, 10, 5);

        TextView commentTitle = new TextView(this);
        commentTitle.setText(R.string.commentTitle);
        commentTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        commentTitle.setTextColor(Color.BLACK);
        commentTitle.setLayoutParams(params);

        View viewDivider = new View(this);
        int dividerHeight = (int) getResources().getDisplayMetrics().density; // 1dp to pixels
        viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));
        viewDivider.setBackgroundColor(Color.GRAY);

        GradientDrawable border = new GradientDrawable();
        border.setColor(0xFFFFFFFF); //white background
        border.setStroke(1, 0xFF000000); //black border with full opacity
        commentsContainer.setBackground(border);
        commentsContainer.addView(commentTitle);
        commentsContainer.addView(viewDivider);

        final EditText editComment = new EditText(this);
        editComment.setLayoutParams(params);
        editComment.setHint(R.string.addCommentHint);
        editComment.setHintTextColor(Color.GRAY);
        editComment.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
        editComment.setTypeface(commentTitle.getTypeface(), Typeface.ITALIC);
        editComment.setTextColor(Color.BLACK);
        editComment.setLayoutParams(params);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        commentsContainer.addView(editComment);

        Button sendButton = new Button(this);
        sendButton.setLayoutParams(params);
        sendButton.setText(R.string.submit);

        commentsContainer.addView(sendButton);

        sendButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String commentText = editComment.getText().toString();
                if (commentText.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Cannot add empty comment!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    UserContactInfo sample_user = new UserContactInfo("commentUser1", "SampleUser", "https://firebasestorage.googleapis.com/v0/b/test-84cb3.appspot.com/o/resources%2F1511445418787.jpg?alt=media&token=79ef569d-b65a-47b6-b1b9-3b32098153ff", "sample@gmail.com");
                    Comment newComment = new Comment(memoryId, sample_user, commentText);
                    addCommentToDatabase(newComment);
                    Toast.makeText(getApplicationContext(), "Comment added!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    private void addCommentToDatabase(Comment newComment) {
        DatabaseHandler.addComment(memoryId, newComment);
    }

    private void fetchMemoryFromDatabase() {
        DatabaseHandler.getMemory(memoryId, new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memory = dataSnapshot.getValue(Memory.class);

                ScrollView parentScroll = findViewById(R.id.parentScroll);

                RelativeLayout memoryImageOrVideo = findViewById(R.id.memoryImageOrVideo);

                ImageView memoryImage = findViewById(R.id.memoryPicture);

                final TextView memoryText = findViewById(R.id.memoryText);

                final VideoView memoryVideo = findViewById(R.id.memoryVideo);

                TextView date = findViewById(R.id.memoryDate);
                date.setText(format.format(memory.getTime()));

                TextView location = findViewById(R.id.memoryLocation);
                location.setText(memory.getSerializableLocation().getLocationName());

                Button playVideo = findViewById(R.id.play_button);
                Button pauseVideo = findViewById(R.id.pause_button);
                Button stopVideo = findViewById(R.id.stop_button);


                if (memory.getPhotoUrl() != null) {
                    memoryImageOrVideo.setVisibility(View.VISIBLE);
                    memoryVideo.setVisibility(View.INVISIBLE);
                    memoryImage.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(memory.getPhotoUrl()).into(memoryImage);
                }

                if (memory.getText() != null) {
                    memoryText.setVisibility(View.VISIBLE);
                    memoryText.setText(memory.getText());
                    memoryText.setMovementMethod(new ScrollingMovementMethod());
                    memoryText.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            v.performClick();
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            return false;
                        }
                    });
                }

                if (memory.getVideoUrl() != null) {
                    memoryImageOrVideo.setVisibility(View.VISIBLE);
                    memoryImage.setVisibility(View.INVISIBLE);
                    memoryVideo.setVisibility(View.VISIBLE);
                    playVideo.setVisibility(View.VISIBLE);
                    pauseVideo.setVisibility(View.VISIBLE);
                    stopVideo.setVisibility(View.VISIBLE);

                    memoryVideo.setVideoPath(memory.getVideoUrl());

                    playVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View vw) {
                            memoryVideo.start();
                        }
                    });

                    pauseVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View vw) {
                            memoryVideo.pause();
                        }
                    });

                    stopVideo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View vw) {
                            memoryVideo.seekTo(0);
                        }
                    });

                }

                TextView author = findViewById(R.id.memoryAuthor);
                author.setText(memory.getUser().getDisplayName());

                ImageView authorPic = findViewById(R.id.memoryAuthorPic);
                new GoogleProfilePictureAsync(authorPic, Uri.parse(memory.getUser().getProfilePhotoUrl())).execute();

                TextView likeNumber = findViewById(R.id.likeNumber);
                likeNumber.setText(memory.getLikes().size() + "");

                commentList = new ArrayList<>(memory.getComments().values());

                if (commentList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    commentAdapter = new CommentAdapter(commentList);
                    commentAdapter.notifyDataSetChanged();

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(commentAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed!");
            }
        });


    }


    /*************************************************
     ******************* Menu Buttons ****************
     *************************************************/
    public void goToCreateMemoryActivity(View view) {
        MenuButtons.goToCreateMemoryActivity(this);
    }

    public void goToPublicMemoryActivity(View view) {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    public void goToMapActivity(View view) {
        MenuButtons.goToMapActivity(this);
    }

    public void goToNotification(View view) {
        MenuButtons.goToNotificationActivity(this);
    }

    public void goToUser(View view) {
        MenuButtons.goToUserProfileActivity(this);
    }
}
