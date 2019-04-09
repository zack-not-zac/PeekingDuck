package peekingduckapp.peekingduck;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEdit extends Fragment {
    private Script script;
    private EditText textField;

    public FragmentEdit() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_script, container, false);
        setHasOptionsMenu(true);

        MainActivity activity = (MainActivity) getActivity();

        activity.getSupportActionBar().setTitle("Script Editor");

        textField = view.findViewById(R.id.edit_script_text);
        textField.setText(FileHandler.load_from_external_storage(script.getScript_path()));

        return view;
    }

    public void setScript(Script script) {
        this.script = script;
    }

}
