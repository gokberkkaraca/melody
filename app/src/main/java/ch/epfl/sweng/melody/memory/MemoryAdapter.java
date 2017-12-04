package ch.epfl.sweng.melody.memory;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.melody.CreateMemoryActivity;
import ch.epfl.sweng.melody.DetailedMemoryActivity;
import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.PublicMemoryActivity;
import ch.epfl.sweng.melody.R;
import ch.epfl.sweng.melody.UserProfileActivity;
import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;
import ch.epfl.sweng.melody.user.User;

import static ch.epfl.sweng.melody.PublicMemoryActivity.addBitmapToMemoryCache;
import static ch.epfl.sweng.melody.PublicMemoryActivity.getBitmapFromMemCache;
import static ch.epfl.sweng.melody.PublicMemoryActivity.mMemoryCache;
import static ch.epfl.sweng.melody.PublicMemoryActivity.saveRecyclerViewPosition;
import static ch.epfl.sweng.melody.UserProfileActivity.EXTRA_USER_ID;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MemoriesViewHolder> {

    private final List<Memory> memoryList;
    private final SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy  hh:mm aa", Locale.FRANCE);

    public MemoryAdapter(List<Memory> memoryList) {
        this.memoryList = memoryList;
    }

    private static Bitmap retrieveVideoFrameFromVideo(String videoPath) {  //delete this if sending thumbnails in createactivity works
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
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
        holder.locationPic.setText(memory.getSerializableLocation().getLocationName());
        holder.locationText.setText(memory.getSerializableLocation().getLocationName());
        holder.likesNumberPublic.setText(String.valueOf(memory.getLikes().size()));
        holder.commentsNumberPublic.setText(String.valueOf(memory.getComments().size()));

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Unlike
                if (memory.getLikes().contains(MainActivity.getUser())) {
                    memory.getLikes().remove(MainActivity.getUser());
                    holder.likeButton.setImageResource(R.mipmap.like_without);
                }
                // Like
                else {
                    memory.getLikes().add(MainActivity.getUser());
                    holder.likeButton.setImageResource(R.mipmap.like_with);
                }

                DatabaseHandler.uploadMemory(memory);
                holder.likesNumberPublic.setText(String.valueOf(memory.getLikes().size()));
            }
        });

        if (memory.getLikes().contains(MainActivity.getUser())) {
            holder.likeButton.setImageResource(R.mipmap.like_with);
        } else {
            holder.likeButton.setImageResource(R.mipmap.like_without); //RecyclerView tries to recycle the views so we have to be sure the views and the pictures are set again if we have to display them again !
        }

        if (memory.getTags().isEmpty()) {
            holder.hashOfMemory.setImageResource(R.mipmap.hash_without);
        }

        final User user = memory.getUser();
        holder.author.setText(user.getDisplayName());
        new GoogleProfilePictureAsync(holder.authorPic, Uri.parse(user.getProfilePhotoUrl())).execute();


        holder.authorPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                intent.putExtra(EXTRA_USER_ID, memory.getUser().getId());
                v.getContext().startActivity(intent);
            }
        });

        if (memory.getMemoryType() == Memory.MemoryType.TEXT) {
            holder.typeOfMemory.setImageResource(R.mipmap.text_type);
            holder.picLayout.setVisibility(View.GONE);
            holder.locationText.setVisibility(View.VISIBLE);
        } else if (memory.getMemoryType() == Memory.MemoryType.PHOTO) {
            holder.typeOfMemory.setImageResource(R.mipmap.photo_type);
            holder.picLayout.setVisibility(View.VISIBLE);
            holder.locationText.setVisibility(View.GONE);
            Picasso.with(holder.itemView.getContext()).load(memory.getPhotoUrl()).into(holder.memoryPic);
        } else if (memory.getMemoryType() == Memory.MemoryType.VIDEO) {
            holder.typeOfMemory.setImageResource(R.mipmap.video);
            holder.picLayout.setVisibility(View.VISIBLE);
            holder.locationText.setVisibility(View.GONE);
            Bitmap thumbnail;
            //if(memory.getPhotoUrl()==null) {
                thumbnail = getBitmapFromMemCache(memory.getId());  //Storing the thumbnails is better than recomputing them everytime
                if (thumbnail == null) {
                    if (mMemoryCache.size() > 5) mMemoryCache.trimToSize(5);
                    thumbnail = retrieveVideoFrameFromVideo(memory.getVideoUrl());
                    addBitmapToMemoryCache(memory.getId(), thumbnail);
                }
                thumbnail = retrieveVideoFrameFromVideo(memory.getVideoUrl());
                holder.memoryPic.setImageBitmap(thumbnail);
                //CreateMemoryActivity.uploadThumbnail(memory.getId(), thumbnail, getView);
            //} else {
            //    Picasso.with(holder.itemView.getContext()).load(memory.getPhotoUrl()).into(holder.memoryPic);
            //}

        }

    }
/*
    private void createAndAddThumbnail() {
        Bitmap thumbnail = retrieveVideoFrameFromVideo(resourceUri.toString());
        Uri thumbnailUri = saveResultToFile("/images", "png", thumbnail);
        DatabaseHandler.uploadResource(thumbnailUri, this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String thumbnailUrl = taskSnapshot.getDownloadUrl().toString();
                memory.setThumbnailUrl(thumbnailUrl);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        }, new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {}
        });
    }*/

    @Override
    public int getItemCount() {
        return memoryList.size();
    }

    class MemoriesViewHolder extends RecyclerView.ViewHolder {
        final TextView author, time, description, locationPic, locationText, likesNumberPublic, commentsNumberPublic;
        final ImageView authorPic, memoryPic, likeButton, typeOfMemory, hashOfMemory;
        final RelativeLayout picLayout;


        MemoriesViewHolder(View view) {
            super(view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        saveRecyclerViewPosition();
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
            locationPic = view.findViewById(R.id.locationPic);
            locationText = view.findViewById(R.id.locationText);
            authorPic = view.findViewById(R.id.authorPic);
            memoryPic = view.findViewById(R.id.memoryPic);
            typeOfMemory = view.findViewById(R.id.typeOfMemory);
            likeButton = view.findViewById(R.id.likeButton);
            likesNumberPublic = view.findViewById(R.id.likesNumberPublic);
            commentsNumberPublic = view.findViewById(R.id.commentsNumberPublic);
            hashOfMemory = view.findViewById(R.id.hashOfMemory);
            picLayout = view.findViewById(R.id.picLayout);
        }
    }

}
