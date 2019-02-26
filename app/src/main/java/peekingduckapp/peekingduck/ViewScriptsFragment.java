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
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewScriptsFragment extends Fragment implements ListAdapter.ClickObjectListener {
    private RecyclerView script_recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private List<Scripts> scripts = MainActivity.scriptsDatabase.ScriptsDao().getScripts();

    public ViewScriptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_scripts, container, false);

        //RecyclerView initialisation
        layoutManager = new LinearLayoutManager(getActivity());
        script_recyclerview = v.findViewById(R.id.script_recycler_view);
        script_recyclerview.setLayoutManager(layoutManager);
        ListAdapter listAdapter = new ListAdapter(scripts, this);
        script_recyclerview.setAdapter(listAdapter);

        return v;
    }

    @Override
    public void OnClickObject(int i) {
        scripts.get(i);             //selects the script at the current position
        Toast.makeText(getActivity(),"click test",Toast.LENGTH_SHORT).show();
    }
}
