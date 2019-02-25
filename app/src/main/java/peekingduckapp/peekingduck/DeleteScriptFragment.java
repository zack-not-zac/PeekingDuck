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
public class DeleteScriptFragment extends Fragment {

    private EditText txtScriptId;
    private Button btn_delete;

    public DeleteScriptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_delete_script, container, false);

        txtScriptId = v.findViewById(R.id.txt_removeID);
        btn_delete = v.findViewById(R.id.button_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(txtScriptId.getText().toString());
                Scripts script = new Scripts();
                script.setScript_id(id);

                MainActivity.scriptsDatabase.ScriptsDao().deleteScript(script);
                Toast.makeText(getActivity(),"Script Successfully Deleted!", Toast.LENGTH_SHORT).show();
                txtScriptId.setText("");
            }
        });

        return v;
    }

}
