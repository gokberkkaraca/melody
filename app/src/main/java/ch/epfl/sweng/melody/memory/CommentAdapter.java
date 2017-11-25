package ch.epfl.sweng.melody.memory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.melody.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder>{

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

        holder.authorName.setText(comment.getAuthorId());
        holder.commentContent.setText(comment.getContent());

    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder {
        final TextView authorName, commentContent;

        CommentsViewHolder(View view) {
            super(view);

            authorName = view.findViewById(R.id.commentAuthorName);
            commentContent = view.findViewById(R.id.commentContent);
        }
    }
}
