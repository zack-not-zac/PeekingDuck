package peekingduckapp.peekingduck;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<Scripts> scripts;
    private ClickObjectListener mClickObjectListener;

    public ListAdapter(List<Scripts> scriptsList, ClickObjectListener clickObjectListener)
    {
        this.scripts = scriptsList;
        this.mClickObjectListener = clickObjectListener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ListViewHolder(v, mClickObjectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder viewHolder, int i) {
        viewHolder.bindView(i);
    }

    @Override
    public int getItemCount() {
        return scripts.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView ItemText;
        ClickObjectListener clickObjectListener;

        ListViewHolder(@NonNull View v, ClickObjectListener clickObjectListener){       //constructor function
            super(v);

            ItemText = v.findViewById(R.id.itemText);
            this.clickObjectListener = clickObjectListener;
        }

        public void bindView(int position)
        {
            String info = "";

            Scripts script = scripts.get(position);

            int id = script.getScript_id();
            String name = script.getScript_name();
            String path = script.getScript_path();

            info += "Id: " + id + "\n Name: " + name + "\n Filepath: " + path;

            ItemText.setText(info);   //Sets the TextView to the output from reading the database
        }

        @Override
        public void onClick(View v) {
            clickObjectListener.OnClickObject(getAdapterPosition());
        }
    }

    public interface ClickObjectListener {
        void OnClickObject(int i);
    }
}
