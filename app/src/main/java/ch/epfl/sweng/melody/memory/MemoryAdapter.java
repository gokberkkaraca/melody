package ch.epfl.sweng.melody.memory;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
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

                //-------------------------------This method should fetch the photo but android won't cast it to MemoryPhoto-----------------
                if (memory.getPhotoUrl() != null) {
                    Picasso.with(holder.itemView.getContext()).load(memory.getPhotoUrl()).into(holder.memoryPic);
                }

                //--------------------------------------This will work but we need to fix the getMemoryType() method first------------------------
                /*if (memory.getMemoryType() == Memory.MemoryType.PHOTO) {
                    Picasso.with(holder.itemView.getContext()).load(memory.getPhoto()).into(holder.memoryPic);
                    holder.description.setText("THIS IS A PHOTO"); //don't fordet to delete this
                }
                else if (memory.getMemoryType() == Memory.MemoryType.VIDEO) {
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(memory.getVideoUrl() , MediaStore.Video.Thumbnails.MICRO_KIND);
                    holder.memoryPic.setImageBitmap(thumbnail);
                    holder.description.setText("THIS IS A VIDEO"); //don't fordet to delete this
                }*/
                //---------------------------------------------------------------------------------------------------------------------------------
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

}
