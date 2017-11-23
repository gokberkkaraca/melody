package ch.epfl.sweng.melody;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.VideoView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.util.MenuButtons;

public class DetailedMemoryActivity extends AppCompatActivity {
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm aa", Locale.FRANCE);
    private Memory memory;
    private String memoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memory_detail);
        memoryId = getIntent().getStringExtra("memoryId");

        TextView memoryText = findViewById(R.id.memoryText);

        RelativeLayout videoSpace = findViewById(R.id.memoryImageOrVideo);

        memoryText.setVisibility(View.GONE);
        videoSpace.setVisibility(View.GONE);

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

        EditText editComment = new EditText(this);
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
    }

    @Override
    public void onBackPressed() {
        MenuButtons.goToPublicMemoryActivity(this);
    }

    private void fetchMemoryFromDatabase() {
        DatabaseHandler.getMemory(memoryId, new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memory = dataSnapshot.getValue(Memory.class);

                ScrollView parentScroll = findViewById(R.id.parentScroll);
                ScrollView textScroll = findViewById(R.id.textScroll);

                RelativeLayout memoryImageOrVideo = findViewById(R.id.memoryImageOrVideo);

                ImageView memoryImage = findViewById(R.id.memoryPicture);

                TextView memoryText = findViewById(R.id.memoryText);

                VideoView memoryVideo = findViewById(R.id.memoryVideo);

                TextView date = findViewById(R.id.memoryDate);
                date.setText(format.format(memory.getTime()));

                TextView location = findViewById(R.id.memoryLocation);
                location.setText(memory.getSerializableLocation().getLocationName());


                if (memory.getPhotoUrl() != null) {
                    memoryImageOrVideo.setVisibility(View.VISIBLE);
                    memoryVideo.setVisibility(View.INVISIBLE);
                    memoryImage.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(memory.getPhotoUrl()).into(memoryImage);
                }

                if (memory.getText() != null) {
                    textScroll.setVisibility(View.VISIBLE);
                    memoryText.setVisibility(View.VISIBLE);
                    memoryText.setText(memory.getText());

                    if (memory.getText().length() > 30) {
                        parentScroll.setOnTouchListener(new View.OnTouchListener() {
                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                findViewById(R.id.textScroll).getParent().requestDisallowInterceptTouchEvent(false);
                                return false;
                            }
                        });

                        textScroll.setOnTouchListener(new View.OnTouchListener() {
                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                                return false;
                            }
                        });
                    }
                }

                if(memory.getVideoUrl() != null){
                    memoryImageOrVideo.setVisibility(View.VISIBLE);
                    memoryImage.setVisibility(View.INVISIBLE);
                    memoryVideo.setVisibility(View.VISIBLE);
                    memoryVideo.setVideoPath(memory.getVideoUrl());
                    memoryText.setText(memory.getVideoUrl());
                    memoryVideo.start();
                }

                TextView author = findViewById(R.id.memoryAuthor);
                author.setText(memory.getUser().getDisplayName());

                ImageView authorPic = findViewById(R.id.memoryAuthorPic);
                new GoogleProfilePictureAsync(authorPic, Uri.parse(memory.getUser().getProfilePhotoUrl())).execute();

                TextView likeNumber = findViewById(R.id.likeNumber);
                likeNumber.setText(memory.getLikes().size() + "");
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
