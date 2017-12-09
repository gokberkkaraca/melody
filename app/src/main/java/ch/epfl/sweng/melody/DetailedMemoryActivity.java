package ch.epfl.sweng.melody;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.user.UserContactInfo;
import ch.epfl.sweng.melody.util.NavigationHandler;

import static ch.epfl.sweng.melody.PublicMemoryActivity.insidePublicActivity;
import static ch.epfl.sweng.melody.UserProfileActivity.EXTRA_USER_ID;

public class DetailedMemoryActivity extends AppCompatActivity {
    private static CommentAdapter commentAdapter;
    private static List<String> tagsList;
    private static User user;
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm aa", Locale.FRANCE);
    private Memory memory;
    private String memoryId;
    private List<Comment> commentList;
    private RecyclerView commentsRecyclerView;
    private ListView tagsListView;
    private ArrayAdapter adapter;
    private ImageView authorPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memory_detail);
        memoryId = getIntent().getStringExtra("memoryId");

        user = MainActivity.getUser();
        TextView memoryText = findViewById(R.id.memoryText);
        RelativeLayout videoSpace = findViewById(R.id.memoryImageOrVideo);

        commentsRecyclerView = findViewById(R.id.comments_recyclerView);
        tagsListView = findViewById(R.id.tags_listView);
        authorPic = findViewById(R.id.memoryAuthorPic);

        memoryText.setVisibility(View.GONE);
        videoSpace.setVisibility(View.GONE);
        commentsRecyclerView.setVisibility(View.GONE);

        fetchMemoryFromDatabase();
        setCommentsContainer();

        tagsList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tagsList);
        tagsListView.setAdapter(adapter);

        authorPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.goToUserProfileActivityFromUserMemory(v.getContext(),memory);
            }
        });

    }

    public void removeMemory(final View view) {
        new AlertDialog.Builder(this)
                .setTitle("Remove Memory ?")
                .setMessage("Are you sure you want to remove this memory?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        if (insidePublicActivity)
                            NavigationHandler.goToPublicMemoryActivity(view.getContext());
                        else {
                            NavigationHandler.goToUserProfileActivityFromUserMemory(view.getContext(),memory);
                        }
                        DatabaseHandler.removeMemory(memoryId);
                        Toast.makeText(getApplicationContext(), "Removing Memory..", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }

    private void setCommentsContainer() {
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
                    UserContactInfo currentUser = new UserContactInfo(user.getId(), user.getDisplayName(), user.getProfilePhotoUrl(), user.getEmail());
                    Comment newComment = new Comment(memoryId, currentUser, commentText);
                    TextView memoryText = findViewById(R.id.memoryText);
                    memoryText.setText(currentUser.getDisplayName());
                    DatabaseHandler.addComment(memoryId, newComment);
                    Toast.makeText(getApplicationContext(), "Comment added!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        NavigationHandler.goToPublicMemoryActivity(this);
    }

    private void fetchMemoryFromDatabase() {
        DatabaseHandler.getMemory(memoryId, new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memory = dataSnapshot.getValue(Memory.class);

                if (memory != null) {

                    RelativeLayout memoryImageOrVideo = findViewById(R.id.memoryImageOrVideo);

                    ImageView memoryImage = findViewById(R.id.memoryPicture);

                    final TextView memoryText = findViewById(R.id.memoryText);

                    final VideoView memoryVideo = findViewById(R.id.memoryVideo);

                    TextView tagsTitle = findViewById(R.id.tags_title);

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

                    final TextView likeNumber = findViewById(R.id.likeNumber);
                    likeNumber.setText(memory.getLikes().size() + "");

                    if (memory.getUser().equals(MainActivity.getUser()))
                        findViewById(R.id.removeMemory).setVisibility(View.VISIBLE);

                    commentList = new ArrayList<>(memory.getComments().values());

                    if (commentList.size() > 0) {
                        commentsRecyclerView.setVisibility(View.VISIBLE);
                        commentAdapter = new CommentAdapter(commentList);
                        commentAdapter.notifyDataSetChanged();

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        commentsRecyclerView.setLayoutManager(mLayoutManager);
                        commentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        commentsRecyclerView.setAdapter(commentAdapter);
                    }

                    if (memory.getTags().size() > 0) {
                        tagsTitle.setVisibility(View.VISIBLE);
                        tagsListView.setVisibility(View.VISIBLE);
                        adapter.clear();
                        tagsList = memory.getTags();
                        adapter.addAll(tagsList);
                        adapter.notifyDataSetChanged();
                    }

                    final ImageButton likeButton = findViewById(R.id.likeButtonDetailed);
                    likeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (memory.getLikes().contains(MainActivity.getUser())) {
                                memory.getLikes().remove(MainActivity.getUser());
                                likeButton.setImageResource(R.mipmap.like_without);
                            } else {
                                memory.getLikes().add(MainActivity.getUser());
                                likeButton.setImageResource(R.mipmap.like_with);
                            }

                            DatabaseHandler.uploadMemory(memory);
                            likeNumber.setText(String.valueOf(memory.getLikes().size()));
                        }
                    });

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
        NavigationHandler.goToCreateMemoryActivity(this);
    }

    public void goToPublicMemoryActivity(View view) {
        NavigationHandler.goToPublicMemoryActivity(this);
    }

    public void goToMapActivity(View view) {
        NavigationHandler.goToMapActivity(this);
    }

    public void goToNotification(View view) {
        NavigationHandler.goToNotificationActivity(this);
    }

    public void goToUser(View view) {
        NavigationHandler.goToUserProfileActivity(this);
    }
}
