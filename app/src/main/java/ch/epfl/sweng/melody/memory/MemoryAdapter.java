package ch.epfl.sweng.melody.memory;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.melody.R;
import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.user.User;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MemoriesViewHolder> {

    private final List<Memory> memoryList;
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm a", Locale.US);


    public MemoryAdapter(List<Memory> memoryList) {
        this.memoryList = memoryList;
    }

    @Override
    public MemoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_list_row, parent, false);

        return new MemoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MemoriesViewHolder holder, int position) {

        final Memory memory = memoryList.get(position);

        holder.time.setText(format.format(memory.getTime()));
        holder.description.setText(memory.getText());
        holder.location.setText(memory.getLocation());

        String userId = memory.getAuthorId();
        DatabaseHandler.getUserInfo(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                holder.author.setText(user.getDisplayName());
                new GoogleProfilePictureAsync(holder.authorPic, Uri.parse(user.getProfilePhotoUrl())).execute();

                if (memory.getMemoryType() == Memory.MemoryType.PHOTO) {
                    Picasso.with(holder.itemView.getContext()).load(memory.getPhotoUrl()).into(holder.memoryPic);
                }
                else if (memory.getMemoryType() == Memory.MemoryType.VIDEO) {
                    //Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(memory.getVideoUrl() , MediaStore.Video.Thumbnails.MICRO_KIND);
                    Bitmap thumbnail =retrieveVideoFrameFromVideo(memory.getVideoUrl());
                    holder.memoryPic.setImageBitmap(thumbnail);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return memoryList.size();
    }

    class MemoriesViewHolder extends RecyclerView.ViewHolder {
        final TextView author, time, description, location;
        final ImageView authorPic, memoryPic;

        MemoriesViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.author);
            time = view.findViewById(R.id.time);
            description = view.findViewById(R.id.description);
            location = view.findViewById(R.id.location);
            authorPic = view.findViewById(R.id.authorPic);
            memoryPic = view.findViewById(R.id.memoryPic);
        }
    }

    //Catch and send exceptions ??
    public static Bitmap retrieveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
        bitmap = mediaMetadataRetriever.getFrameAtTime();

        if (mediaMetadataRetriever != null) {
            mediaMetadataRetriever.release();
        }

        return bitmap;
    }

}
