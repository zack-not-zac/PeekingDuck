package peekingduckapp.peekingduck;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewScriptsFragment extends Fragment {
    private RecyclerView script_recyclerview;
    private RecyclerView.LayoutManager layoutManager;

    public ViewScriptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_scripts, container, false);
        script_recyclerview = v.findViewById(R.id.script_recycler_view);
        ListAdapter listAdapter = new ListAdapter();
        script_recyclerview.setAdapter(listAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        script_recyclerview.setLayoutManager(layoutManager);

/*        List<Scripts> scripts = MainActivity.scriptsDatabase.ScriptsDao().getScripts();

        String info = "";

        for (Scripts script : scripts)
        {
            int id = script.getScript_id();
            String name = script.getScript_name();
            String path = script.getScript_path();

            info += "\n\n" + "Id: " + id + "\n Name: " + name + "\n Filepath: " + path;

        }

        txtScripts.setText(info);   //Sets the TextView to the output from reading the database*/

        return v;
    }

}
