package ch.epfl.sweng.melody.user;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.melody.R;
import ch.epfl.sweng.melody.UserProfileActivity;
import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;

import static ch.epfl.sweng.melody.UserProfileActivity.EXTRA_USER_ID;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendsViewHolder> {
    private final List<UserContactInfo> friendList;

    public FriendAdapter(List<UserContactInfo> friendList) {
        this.friendList = friendList;
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list_row, parent, false);
        return new FriendsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FriendsViewHolder holder, int position) {

        final UserContactInfo friend = friendList.get(position);

        holder.friendName.setText(friend.getDisplayName());
        holder.friendMail.setText(friend.getEmail());

        new GoogleProfilePictureAsync(holder.friendPic, Uri.parse(friend.getProfilePhotoUrl())).execute();

    }

    @Override
    public int getItemCount() {
        return friendList == null ? 0 : friendList.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {
        final TextView friendName, friendMail;
        final ImageView friendPic;

        FriendsViewHolder(View view) {
            super(view);

            friendName = view.findViewById(R.id.friendName);
            friendMail = view.findViewById(R.id.friendMail);
            friendPic = view.findViewById(R.id.friendPic);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        UserContactInfo clickedFriend = friendList.get(pos);
                    Intent intent = new Intent(v.getContext(), UserProfileActivity.class);
                    intent.putExtra(EXTRA_USER_ID, clickedFriend.getUserId());
                    v.getContext().startActivity(intent);
                    }
                }
            });

        }
    }

}
