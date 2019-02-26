package peekingduckapp.peekingduck;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {

    public List<Scripts> scripts = MainActivity.scriptsDatabase.ScriptsDao().getScripts();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ListViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
/*        int counter = 0;      TODO old code - remove
        List<Scripts> scripts = MainActivity.scriptsDatabase.ScriptsDao().getScripts();

        for (Scripts script : scripts)
        {
            counter++;
        }*/

        return scripts.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView ItemText;

        ListViewHolder(View v){
            super(v);

            ItemText = v.findViewById(R.id.itemText);
            v.setOnClickListener(this);
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

        }
    }
}
