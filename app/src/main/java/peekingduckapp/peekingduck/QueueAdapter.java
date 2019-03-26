package peekingduckapp.peekingduck;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.QueueViewHolder> {

    private List<QueueItem> queue = new ArrayList<>();
    private onItemClickedListener listener;
    private deleteBtnClickedListener delete_listener;

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
        private ImageButton delete_btn;

        QueueViewHolder(@NonNull View v){       //constructor function
            super(v);

            ItemText = v.findViewById(R.id.itemText);
            delete_btn = v.findViewById(R.id.delete_btn);

            delete_btn.setImageResource(R.drawable.ic_minus);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(queue.get(pos));       //passes the note at the position clicked
                    }
                }
            });
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    delete_listener.onDeleteClick(queue.get(pos));
                    notifyItemRemoved(pos);
                }
            });
        }

        public void bindView(int position)
        {
            QueueItem item = queue.get(position);
            String script_body = item.getScript_body();

            ItemText.setText(script_body);   //Sets the TextView to the output from reading the database
        }
    }

    public interface onItemClickedListener {        //listens for a note to be clicked
        void onItemClick(QueueItem item);
    }

    public void setOnItemClickedListener(onItemClickedListener listener) {
        this.listener = listener;
    }
    public interface deleteBtnClickedListener {
        void onDeleteClick(QueueItem item);
    }

    public void setOnDeleteClickedListener(deleteBtnClickedListener delete_listener){
        this.delete_listener = delete_listener;
    }
}