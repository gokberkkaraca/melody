package ch.epfl.sweng.melody.memory;

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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder> {

    private final List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_row, parent, false);
        return new CommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsViewHolder holder, int position) {

        final Comment comment = commentList.get(position);

        holder.authorName.setText(comment.getUserContactInfo().getDisplayName());
        holder.commentContent.setText(comment.getContent());

        new GoogleProfilePictureAsync(holder.authorPic, Uri.parse(comment.getUserContactInfo().getProfilePhotoUrl())).execute();
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        final TextView authorName, commentContent;
        final ImageView authorPic;

        CommentsViewHolder(View view) {
            super(view);

            authorPic = view.findViewById(R.id.commentAuthorPic);
            authorName = view.findViewById(R.id.commentAuthorName);
            commentContent = view.findViewById(R.id.commentContent);
        }
    }
}
