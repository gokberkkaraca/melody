package ch.epfl.sweng.melody;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.memory.Memory;
import ch.epfl.sweng.melody.memory.MemoryAdapter;
import ch.epfl.sweng.melody.user.User;
import ch.epfl.sweng.melody.util.MenuButtons;

public class DetailedMemoryActivity extends AppCompatActivity {
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm a", Locale.US);
    User user;
    private Memory memory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memory_detail);
        fetchMemoryFromDatabase(getIntent().getStringExtra("memoryId"));

        user = (User) getIntent().getExtras().getSerializable(MainActivity.USER_INFO);


        LinearLayout commentsContainer = findViewById(R.id.memoryComments) ;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 5, 10, 5);

        TextView commentTitle = new TextView(this);
        commentTitle.setText(R.string.commentTitle);
        commentTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        commentTitle.setTextColor(Color.BLACK);
        commentTitle.setLayoutParams(params);

        View viewDivider = new View (this);
        int dividerHeight = (int) getResources().getDisplayMetrics().density ; // 1dp to pixels
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

        commentsContainer.addView(editComment);

        Button sendButton = new Button(this);
        sendButton.setLayoutParams(params);
        sendButton.setText(R.string.submit);

        commentsContainer.addView(sendButton);
    }

    private void fetchMemoryFromDatabase(final String memoryId) {
        DatabaseHandler.getMemory(memoryId, new ValueEventListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memory = dataSnapshot.getValue(Memory.class);

                TextView date = (TextView) findViewById(R.id.memoryDate);
                date.setText(format.format(memory.getTime()));

                TextView location = (TextView) findViewById(R.id.memoryLocation);
                location.setText(memory.getLocation());

                ImageView imageView = (ImageView) findViewById(R.id.memoryPicture);

                if (memory.getPhotoUrl() != null) {
                    Picasso.with(getApplicationContext()).load(memory.getPhotoUrl()).into(imageView);
                }

                fetchUserInfo(memory.getAuthorId());

                TextView likeNumber = findViewById(R.id.likeNumber);
                likeNumber.setText(memory.getLikeNumber() +  "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed!");
            }
        });
    }

    private void fetchUserInfo(final String userId) {
        DatabaseHandler.getUserInfo(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                TextView author = (TextView) findViewById(R.id.memoryAuthor);
                author.setText(user.getDisplayName());

                ImageView authorPic = (ImageView) findViewById(R.id.memoryAuthorPic);
                new GoogleProfilePictureAsync(authorPic, Uri.parse(user.getProfilePhotoUrl())).execute();
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
        MenuButtons.goToCreateMemoryActivity(this, user);
    }

    public void goToPublicMemoryActivity(View view) {
        MenuButtons.goToPublicMemoryActivity(this, user);
    }

    public void goToMapActivity(View view) {
        MenuButtons.goToMapActivity(this, user);
    }

    public void goToNotification(View view) {
        MenuButtons.goToNotificationActivity(this, user);
    }

    public void goToUser(View view) {
        MenuButtons.goToUserProfileActivity(this, user);
    }
}