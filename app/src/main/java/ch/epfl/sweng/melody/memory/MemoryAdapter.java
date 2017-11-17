package ch.epfl.sweng.melody.memory;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.melody.DetailedMemoryActivity;
import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.R;
import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.user.User;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MemoriesViewHolder> {

    private final List<Memory> memoryList;
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm aa", Locale.FRANCE);

    public MemoryAdapter(List<Memory> memoryList) {
        this.memoryList = memoryList;
    }

    //Catch and send exceptions ??
    private static Bitmap retrieveVideoFrameFromVideo(String videoPath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
        bitmap = mediaMetadataRetriever.getFrameAtTime();

        mediaMetadataRetriever.release();

        return bitmap;
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
        holder.location.setText(memory.getSerializableLocation().getLocationName());
        holder.likesNumberPublic.setText(String.valueOf(memory.getLikeNumber()));
        holder.commentsNumberPublic.setText(String.valueOf(memory.getCommentNumber()));

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memory.likeAction(MainActivity.getUser());

                if (memory.isLikedByUser(MainActivity.getUser())) {
                    holder.likeButton.setImageResource(R.mipmap.like_with);
                } else {
                    holder.likeButton.setImageResource(R.mipmap.like_without);
                }
                holder.likesNumberPublic.setText(String.valueOf(memory.getLikeNumber()));
            }
        });

        if (memory.isLikedByUser(MainActivity.getUser())) {
            holder.likeButton.setImageResource(R.mipmap.like_with);
        }

        User user = memory.getUser();
        holder.author.setText(user.getDisplayName());
        new GoogleProfilePictureAsync(holder.authorPic, Uri.parse(user.getProfilePhotoUrl())).execute();

        if (memory.getMemoryType() == Memory.MemoryType.TEXT) {
            holder.typeOfMemory.setImageResource(R.mipmap.text_type);
            holder.memoryPic.setImageResource(R.mipmap.writing_type_image);
        } else if (memory.getMemoryType() == Memory.MemoryType.PHOTO) {
            Picasso.with(holder.itemView.getContext()).load(memory.getPhotoUrl()).into(holder.memoryPic);
        } else if (memory.getMemoryType() == Memory.MemoryType.VIDEO) {
            holder.typeOfMemory.setImageResource(R.mipmap.video);
            //Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(memory.getVideoUrl() , MediaStore.Video.Thumbnails.MICRO_KIND);
            Bitmap thumbnail = retrieveVideoFrameFromVideo(memory.getVideoUrl());
            holder.memoryPic.setImageBitmap(thumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return memoryList.size();
    }

    class MemoriesViewHolder extends RecyclerView.ViewHolder {
        final TextView author, time, description, location, likesNumberPublic, commentsNumberPublic;
        final ImageView authorPic, memoryPic, likeButton, typeOfMemory;


        MemoriesViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        Memory clickedMemory = memoryList.get(pos);
                        Intent intent = new Intent(v.getContext(), DetailedMemoryActivity.class);
                        intent.putExtra("memoryId", clickedMemory.getId());
                        v.getContext().startActivity(intent);
                    }
                }
            });

            author = view.findViewById(R.id.author);
            time = view.findViewById(R.id.time);
            description = view.findViewById(R.id.description);
            location = view.findViewById(R.id.location);
            authorPic = view.findViewById(R.id.authorPic);
            memoryPic = view.findViewById(R.id.memoryPic);
            typeOfMemory = view.findViewById(R.id.typeOfMemory);
            likeButton = view.findViewById(R.id.likeButton);
            likesNumberPublic = view.findViewById(R.id.likesNumberPublic);
            commentsNumberPublic = view.findViewById(R.id.commentsNumberPublic);
        }
    }

}
