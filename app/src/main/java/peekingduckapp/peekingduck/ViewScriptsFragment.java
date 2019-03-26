package peekingduckapp.peekingduck;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewScriptsFragment extends Fragment {
    private RecyclerView script_recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    //private List<Script> scripts = MainActivity.scriptsDatabase.ScriptsDao().getScripts();
    private ListAdapter listAdapter;
    private ScriptViewModel scriptVM;

    public ViewScriptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_scripts, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Scripts");

        //RecyclerView initialisation
        layoutManager = new LinearLayoutManager(getActivity());
        script_recyclerview = v.findViewById(R.id.script_recycler_view);
        script_recyclerview.setLayoutManager(layoutManager);
        listAdapter = new ListAdapter();
        script_recyclerview.setAdapter(listAdapter);

        scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);

        scriptVM.getAllScripts().observe(getActivity(), new Observer<List<Script>>() {
            @Override
            public void onChanged(@Nullable List<Script> scripts) {
                listAdapter.setScripts(scripts);
            }
        });

        listAdapter.setOnItemClickedListener(new ListAdapter.onItemClickedListener() {
            @Override
            public void onItemClick(Script script) {
                //TODO: code for onClick goes here

                Toast.makeText(getContext(),"Item Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
