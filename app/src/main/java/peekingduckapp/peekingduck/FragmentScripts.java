package peekingduckapp.peekingduck;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class FragmentScripts extends Fragment implements View.OnClickListener {
    private RecyclerView script_recyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private ScriptAdapter scriptAdapter;
    private ScriptViewModel scriptVM;
    private TextView no_scripts;
    private FloatingActionButton fab;

    public FragmentScripts() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_scripts, container, false);
        setHasOptionsMenu(false);

        final MainActivity activity = (MainActivity) getActivity();

        layoutManager = new LinearLayoutManager(activity);
        script_recyclerview = view.findViewById(R.id.script_recycler_view);
        no_scripts = view.findViewById(R.id.no_scripts);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(this);
        script_recyclerview.setLayoutManager(layoutManager);

        activity.getSupportActionBar().setTitle("Script");

        scriptAdapter = new ScriptAdapter(getContext());
        script_recyclerview.setAdapter(scriptAdapter);

        scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);
        scriptVM.getAllScripts().observe(activity, new Observer<List<Script>>() {
            @Override
            public void onChanged(@Nullable List<Script> scripts) {
                scriptAdapter.setScripts(scripts);
                toggle_view(scripts.size() == 0);
            }
        });

        scriptAdapter.setOnItemClickedListener(new ScriptAdapter.onItemClickedListener() {
            @Override
            public void onItemClick(Script script) {
                //QueueItem item = new QueueItem(script.getScript_name(), FileHandler.load_from_external_storage(script.getScript_path()), 0);
                //scriptVM.addToQueue(item);
                activity.switch_fragment_to_edit(script);

                //Toast.makeText(getContext(), "Script added to queue", Toast.LENGTH_SHORT).show();
            }
        });

        scriptAdapter.setOnDeleteClickedListener(new ScriptAdapter.deleteBtnClickedListener() {
            @Override
            public void onDeleteClick(final Script script) {
                scriptVM.delete(script);
            }
        });

        return view;
    }

    private void toggle_view(boolean isEmpty) {
        script_recyclerview.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        no_scripts.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab:
                ((MainActivity)getActivity()).add_new_script();
                break;
        }
    }
}
