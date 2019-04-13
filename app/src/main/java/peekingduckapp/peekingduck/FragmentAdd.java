package peekingduckapp.peekingduck;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class FragmentAdd extends Fragment {
    private EditText text_name;
    private EditText text_script;
    private ScriptViewModel scriptVM;
    private String payload = "";

    public FragmentAdd() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        setHasOptionsMenu(true);

        scriptVM = ViewModelProviders.of(this).get(ScriptViewModel.class);

        text_name = view.findViewById(R.id.add_name);
        text_script = view.findViewById(R.id.add_payload);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SCRIPT", "onResume payload: " + payload);
        text_script.setText(payload);
        payload = "";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_script:
                add_script();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void add_script() {
        String script_name = text_name.getText().toString();
        String script_payload = text_script.getText().toString();
        script_name.replaceAll("/[^A-Za-z0-9]/", "");
        if(script_name.length() == 0 || script_payload.length() == 0) {
            Toast.makeText(getActivity(), "Invalid name / script.", Toast.LENGTH_LONG).show();
        } else {
            String file_name = "scripts/" + script_name.toLowerCase() + ".txt";
            FileHandler.save_file_to_external_storage(file_name, script_payload);
            Script script = new Script(script_name, file_name);
            scriptVM.insert(script);
            Toast.makeText(getActivity(), "Script Added", Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();
        }
    }

    public void set_file_path(String path) {
        String content = FileHandler.load_from_external_storage(path);
        Log.d("SCRIPT", "Setting payload: " + content);
        this.payload = content;
        if(text_script != null) {
            Log.d("SCRIPT", "Text null");
            text_script.setText(payload);
        }
    }
}
