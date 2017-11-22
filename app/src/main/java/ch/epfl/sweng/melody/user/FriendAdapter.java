package ch.epfl.sweng.melody.user;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.melody.R;
import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendsViewHolder> {
    private final List<User> friendList;

    public FriendAdapter(List<User> friendList) {
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

        final User friend = friendList.get(position);

        holder.friendName.setText(friend.getDisplayName());
        holder.friendMail.setText(friend.getEmail());

        new GoogleProfilePictureAsync(holder.friendPic, Uri.parse(friend.getProfilePhotoUrl())).execute();

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder {
        final TextView friendName, friendMail;
        final ImageView friendPic;

        FriendsViewHolder(View view) {
            super(view);

            friendName = view.findViewById(R.id.friendName);
            friendMail = view.findViewById(R.id.friendMail);
            friendPic = view.findViewById(R.id.friendPic);
        }
    }

}
