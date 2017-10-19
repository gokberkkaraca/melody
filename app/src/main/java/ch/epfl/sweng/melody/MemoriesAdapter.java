package ch.epfl.sweng.melody;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ch.epfl.sweng.melody.memory.Memory;

public class MemoriesAdapter extends RecyclerView.Adapter<MemoriesAdapter.MemoriesViewHolder> {

    private List<Memory> memoryList;

    public class MemoriesViewHolder extends RecyclerView.ViewHolder {
        public TextView author, time, description, location;

        public MemoriesViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.author);
            time = (TextView) view.findViewById(R.id.time);
            description = (TextView) view.findViewById(R.id.description);
            location = (TextView) view.findViewById(R.id.location);
        }
    }

    public MemoriesAdapter(List<Memory> memoryList) {
        this.memoryList = memoryList;
    }

    @Override
    public MemoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memory_list_row, parent, false);

        return new MemoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemoriesViewHolder holder, int position) {
        Memory memory = memoryList.get(position);
        holder.author.setText(memory.getAuthor().toString());
        holder.time.setText(memory.getTime().toString());
        holder.description.setText(memory.getText());
        holder.location.setText(memory.getLocation());
    }

    @Override
    public int getItemCount() {
        return memoryList.size();
    }

}
























