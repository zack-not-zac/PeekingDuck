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
    private QueueItem queueItem;
    private boolean edit_mode = false;
    private MainActivity activity;

    public FragmentEdit() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_script, container, false);
        setHasOptionsMenu(true);
//        setRetainInstance(true);

        activity = (MainActivity) getActivity();

        activity.getSupportActionBar().setTitle("Script Editor");

        textField = view.findViewById(R.id.edit_script_text);
//        textField.setText(FileHandler.load_from_app_external_storage(script.getScript_path()));

        scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);

        return view;
    }

    @Override
    public void onResume() {
        if(!edit_mode) {
            textField.setText(FileHandler.load_from_app_external_storage(script.getScript_path()));
        } else if(edit_mode) {
            textField.setText(queueItem.getScript_body());
        }
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit, menu);
        MenuItem item = menu.findItem(R.id.edit_add);
        item.setIcon(edit_mode ? R.drawable.ic_save : R.drawable.ic_add);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
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
        this.edit_mode = false;
    }

    public void setQueueItem(QueueItem queueItem) {
        this.queueItem = queueItem;
        this.edit_mode = true;
    }

    private void add_to_queue() {
        if(edit_mode) {
            scriptVM.updateQueueItem(textField.getText().toString(), queueItem.getID());
            queueItem.setScript_body(textField.getText().toString());
            Toast.makeText(getContext(), "Queue Item saved", Toast.LENGTH_LONG).show();
            activity.onBackPressed();
        } else {
            QueueItem item = new QueueItem(script.getScript_name(), textField.getText().toString(), scriptVM.countQueueItems());
            scriptVM.addToQueue(item);

            Toast.makeText(getContext(), "Script added to the queue", Toast.LENGTH_LONG).show();
        }
    }

}
