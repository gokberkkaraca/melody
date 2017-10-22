package ch.epfl.sweng.melody.memory;

import android.net.Uri;
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
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.melody.R;
import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.user.User;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MemoriesViewHolder> {

    private List<Memory> memoryList;
    private SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm a");


    class MemoriesViewHolder extends RecyclerView.ViewHolder {
        TextView author, time, description, location;
        ImageView authorPic, memoryPic;

        MemoriesViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.author);
            time = (TextView) view.findViewById(R.id.time);
            description = (TextView) view.findViewById(R.id.description);
            location = (TextView) view.findViewById(R.id.location);
            authorPic = (ImageView) view.findViewById(R.id.authorPic);
            memoryPic = (ImageView) view.findViewById(R.id.memoryPic);
        }
    }

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

        String userId = memory.getAuthor();
        DatabaseHandler.getUserInfo(userId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                holder.author.setText(user.getDisplayName());
                new GoogleProfilePictureAsync(holder.authorPic, Uri.parse(user.getProfilePhotoUrl())).execute();
                //-------------------------------This method should fetch the photo but android won't cast it to MemoryPhoto-----------------
                //if(memory.getType().equals(Memory.Type.PHOTO)) {
                   //Picasso.with(holder.itemView.getContext()).load(((MemoryPhoto) memory).getPhotos().get(0)).into(holder.memoryPic);
                //}
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

}
