package peekingduckapp.peekingduck;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddScriptFragment extends Fragment implements View.OnClickListener {

    private Button btn_AddScript;
    private Button btn_ViewScript;

    public AddScriptFragment() {
        // Required empty public constructor
    }

    //TODO once debugging is complete, the ViewScriptsFragment will replace this one, the AddScripts will be done from the nav drawer, and deleting them will be a button on each list item

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_script, container, false);
        btn_AddScript = v.findViewById(R.id.button_addscript);
        btn_AddScript.setOnClickListener(this);

        btn_ViewScript = v.findViewById(R.id.button_viewscripts);
        btn_ViewScript.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_addscript:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new AddToDatabaseFragment()).
                        addToBackStack(null).commit();
                break;
            case R.id.button_viewscripts:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new ViewScriptsFragment()).
                        addToBackStack(null).commit();
                break;
        }

    }
}
