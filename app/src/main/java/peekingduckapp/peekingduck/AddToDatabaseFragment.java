package peekingduckapp.peekingduck;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddToDatabaseFragment extends Fragment {

    private EditText scriptName, scriptId, scriptPath;
    private Button btn_save;

    public AddToDatabaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_to_database, container, false);

        //TODO: The filepath will need retrieved through file browser, the ID will need to be auto generated & incremented to avoid clashes, name could be retrieved from file name

        scriptName = v.findViewById(R.id.script_name);
        scriptId = v.findViewById(R.id.script_id);
        scriptPath = v.findViewById(R.id.script_path);
        btn_save = v.findViewById(R.id.button_save);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(scriptId.getText().toString());
                String name = scriptName.getText().toString();
                String path = scriptPath.getText().toString();

                Scripts script = new Scripts();
                script.setScript_id(id);
                script.setScript_name(name);
                script.setScript_path(path);

                MainActivity.scriptsDatabase.ScriptsDao().addScript(script);    //Adds "script" item data to the database
                Toast.makeText(getActivity(),"Script Added Successfully!", Toast.LENGTH_SHORT).show();

                //clears the text fields
                scriptName.setText("");
                scriptId.setText("");
                scriptPath.setText("");
            }
        });

        return v;
    }

}
