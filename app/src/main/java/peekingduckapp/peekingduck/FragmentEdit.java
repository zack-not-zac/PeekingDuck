package peekingduckapp.peekingduck;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEdit extends Fragment {
    private Script script;
    private EditText textField;
    private ScriptViewModel scriptVM;

    public FragmentEdit() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_script, container, false);
        setHasOptionsMenu(true);

        MainActivity activity = (MainActivity) getActivity();

        activity.getSupportActionBar().setTitle("Script Editor");

        textField = view.findViewById(R.id.edit_script_text);
        textField.setText(FileHandler.load_from_external_storage(script.getScript_path()));

        scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_add:
                add_to_queue();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setScript(Script script) {
        this.script = script;
    }

    private void add_to_queue() {
        QueueItem item = new QueueItem(script.getScript_name(), FileHandler.load_from_external_storage(script.getScript_path()), 0);
        scriptVM.addToQueue(item);

        Toast.makeText(getContext(), "Script added to the queue", Toast.LENGTH_LONG).show();

    }

}
