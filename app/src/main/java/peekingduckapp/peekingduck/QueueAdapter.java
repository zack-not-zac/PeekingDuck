package peekingduckapp.peekingduck;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {

    private List<QueueItem> queue = new ArrayList<>();
    private onItemClickedListener listener;

    @NonNull
    @Override
    public QueueViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new QueueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QueueViewHolder viewHolder, int i) {
        viewHolder.bindView(i);
    }

    public void setQueue(List<QueueItem> queue) {
        this.queue = queue;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return queue.size();
    }

    public QueueItem getQueueItem(int pos)
    {
        return queue.get(pos);
    }

    public class QueueViewHolder extends RecyclerView.ViewHolder
    {
        private TextView ItemText;
        private TextView IDText;

        QueueViewHolder(@NonNull View v){       //constructor function
            super(v);

            ItemText = v.findViewById(R.id.itemText);
            IDText = v.findViewById(R.id.idText);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(queue.get(pos));       //passes the note at the position clicked
                    }
                }
            });
        }

        public void bindView(int position)
        {
            QueueItem item = queue.get(position);

            int id = item.getID();
            String script_body = item.getScript_body();

            ItemText.setText(script_body);   //Sets the TextView to the output from reading the database
            IDText.setText("ID: " + id);
        }
    }

    public interface onItemClickedListener {        //listens for a note to be clicked
        void onItemClick(QueueItem item);
    }

    public void setOnItemClickedListener(onItemClickedListener listener) {
        this.listener = listener;

    }
}