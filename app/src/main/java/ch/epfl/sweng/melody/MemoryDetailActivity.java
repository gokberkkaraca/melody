package ch.epfl.sweng.melody;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import ch.epfl.sweng.melody.user.User;

public class MemoryDetailActivity extends AppCompatActivity {
    private Memory memory;
    User user;
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm a", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_memory_detail);
        fetchMemoryFromDatabase(getIntent().getStringExtra("memoryId"));

        user = (User) getIntent().getExtras().getSerializable(MainActivity.USER_INFO);
    }

    private void fetchMemoryFromDatabase(final String memoryId) {
        DatabaseHandler.getMemory(memoryId, new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memory = dataSnapshot.getValue(Memory.class);

                TextView date = (TextView) findViewById(R.id.memoryDate);
                date.setText(format.format(memory.getTime()));

                TextView location = (TextView) findViewById(R.id.memoryLocation);
                location.setText(memory.getLocation());

                ImageView imageView = (ImageView) findViewById(R.id.memoryPicture);

                VideoView videoView = (VideoView) findViewById(R.id.memoryVideo);


                if(memory.getPhotoUrl() != null) {
                    imageView.setVisibility(View.VISIBLE);
                    Picasso.with(getApplicationContext()).load(memory.getPhotoUrl()).into(imageView);
                }

                if(memory.getVideoUrl() != null) {
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoPath(memory.getVideoUrl());
                    videoView.start();
                }

                fetchUserInfo(memory.getAuthorId());
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

    public void goToPublicMemory(View view) {
        Intent intent = new Intent(this, PublicMemoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void addNewMemory(View view) {
        Intent intent = new Intent(this, CreateMemoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goToUser(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(MainActivity.USER_INFO, user);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
