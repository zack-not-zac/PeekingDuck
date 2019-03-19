package peekingduckapp.peekingduck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class InterpreterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_test_interpreter;
    private String script_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpreter);

        btn_test_interpreter = (Button) findViewById(R.id.btn_test_interpreter);
        btn_test_interpreter.setOnClickListener(this);
        script_path = getIntent().getStringExtra("script_path");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_interpreter:
                test_script();
                break;
        }
    }

    private void test_script() {
        ArrayList<String> script = new ArrayList<String>(Arrays.asList("GUI r",
                "DELAY 50",
                "STRING notepad.exe",
                "ENTER",
                "DELAY 100",
                "STRING Hello World",
                "ALT f",
                "STRING s",
                "REM alt-f pulls up the File menu and s saves. This two keystroke combo is why ALT is jealous of CONTROL's leetness and CTRL+S"));
        Interpreter interpreter = new Interpreter(script, script_path);
        interpreter.run();
    }
}
