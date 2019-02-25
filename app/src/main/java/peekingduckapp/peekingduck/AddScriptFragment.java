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

    public AddScriptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_script, container, false);
        btn_AddScript = view.findViewById(R.id.button_addscript);
        btn_AddScript.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_addscript:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,new AddToDatabaseFragment()).
                        addToBackStack(null).commit();
                break;
        }

    }
}
