package ch.epfl.sweng.melody.memory;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.melody.MainActivity;
import ch.epfl.sweng.melody.R;
import ch.epfl.sweng.melody.account.GoogleProfilePictureAsync;
import ch.epfl.sweng.melody.database.DatabaseHandler;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder> {

    private final List<Comment> commentList;
    private View itemView;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_row, parent, false);
        return new CommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsViewHolder holder, final int position) {

        final Comment comment = commentList.get(position);

        holder.authorName.setText(comment.getUserContactInfo().getDisplayName());
        holder.commentContent.setText(comment.getContent());

        if (MainActivity.getUser().getId().equals(comment.getUserContactInfo().getUserId()))
            holder.removeCommentButton.setVisibility(View.VISIBLE);
        else
            holder.removeCommentButton.setVisibility(View.INVISIBLE);

        holder.removeCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemView.setVisibility(View.GONE);
                DatabaseHandler.removeComment(comment);

            }
        });

        new GoogleProfilePictureAsync(holder.authorPic, Uri.parse(comment.getUserContactInfo().getProfilePhotoUrl())).execute();
    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        final TextView authorName, commentContent;
        final ImageView authorPic;
        final ImageButton removeCommentButton;

        CommentsViewHolder(View view) {
            super(view);

            authorPic = view.findViewById(R.id.commentAuthorPic);
            authorName = view.findViewById(R.id.commentAuthorName);
            commentContent = view.findViewById(R.id.commentContent);
            removeCommentButton = view.findViewById(R.id.removeComment);
        }
    }
}
