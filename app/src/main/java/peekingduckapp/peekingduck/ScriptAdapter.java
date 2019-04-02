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

public class ScriptAdapter extends RecyclerView.Adapter<ScriptAdapter.ListViewHolder> {

    private List<Script> scripts = new ArrayList<>();
    private onItemClickedListener listener;
    private ImageButton delete_btn;
    private deleteBtnClickedListener delete_listener;
    private TextView queue_txt;

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        queue_txt = v.findViewById(R.id.queuePos_text);
        queue_txt.setVisibility(v.GONE);

        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder viewHolder, int i) {
        viewHolder.bindView(i);
    }

    public void setScripts(List<Script> scripts) {
        this.scripts = scripts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }

    public Script getScript(int pos)
    {
        return scripts.get(pos);
    }

    /*public int getPos(Script script)
    {
        for (int x = 0; x < scripts.size(); x++)
        {
            if (scripts.get(x) == script)
            {
                return x;
            }
        }

        return -1;
    }*/


    public class ListViewHolder extends RecyclerView.ViewHolder
    {
        private TextView ItemText;

        ListViewHolder(@NonNull View v){       //constructor function
            super(v);

            ItemText = v.findViewById(R.id.itemText);
            delete_btn = v.findViewById(R.id.delete_btn);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(scripts.get(pos));       //passes the note at the position clicked
                    }
                }
            });

            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    delete_listener.onDeleteClick(scripts.get(pos));
                    notifyItemRemoved(pos);
                }
            });
        }

        public void bindView(int position)
        {
            Script script = scripts.get(position);

            String name = script.getScript_name();
            //String path = script.getScript_path();

            ItemText.setText(name);   //Sets the TextView to the output from reading the database
        }
    }

    public interface onItemClickedListener {        //listens for a note to be clicked
        void onItemClick(Script script);
    }

    public interface deleteBtnClickedListener {
        void onDeleteClick(Script script);
    }

    public void setOnItemClickedListener(onItemClickedListener listener) {
        this.listener = listener;
    }

    public void setOnDeleteClickedListener(deleteBtnClickedListener delete_listener){
        this.delete_listener = delete_listener;
    }
}
