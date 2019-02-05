package peekingduckapp.peekingduck;

import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
    private String script_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Make a small toast
     * Used mostly for debugging.
     * @param txt
     */
    protected void make_toast(String txt) {
        Toast.makeText(MainActivity.this, txt, Toast.LENGTH_LONG).show();
    }

    /**
     * Request root access.
     * Should be called on initial app run, to ensure access for other methods.
     * Attempts to ask again if root not granted, untested though.
     * @param attempts
     */
    protected void request_root_access(int attempts) {
        try {
            final Process su = Runtime.getRuntime().exec("su");
        } catch (IOException e) {
            if(attempts == 0) {
                request_root_access(attempts++);
            } else {
                make_toast("Unable to gain required root access");
            }
        }
    }

    /**
     * Unpacks the resource hid_gadget_test file and attempts to set it's permission to 755 so it's executable
     * Requires root
     */
    protected void unbundle_file() {
        InputStream is = getResources().openRawResource(getResources().getIdentifier("hid_gadget_test", "raw", getPackageName()));
        boolean successful_copy = false;

        File file = new File(getFilesDir(), "hid_gadget_test");
        script_path = file.getAbsolutePath();

        // If file is not already unpacked
        if (!file.exists()) {
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
        } else {
            make_toast("File Exists");
        }
    }

    /**
     * Send raw keystrokes to the PC.
     * Handles primitive keys in the current state, such as lower/uppercase characters, numbers, spaces and newlines.
     * Will be expanded upon.
     * @param text Text to send.
     */
    protected void send_keystrokes(String text) {
        // From test activity, currently hard coded
//        TextView txt = findViewById(R.id.input_test);
//        String text = txt.getText().toString();
        char[] keys = text.toCharArray();

        try{
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            for (int i = 0; i < keys.length; i++) {
                String key_seq;
                if (keys[i] == ' ') key_seq = "space";
                else if(keys[i] == '\n') key_seq = "enter";
                else if((int) keys[i] > 64 && (int) keys[i] < 91) key_seq = "left-shift " + ("" + keys[i]).toLowerCase();
                else key_seq = "" + keys[i];

                // Sending the keystroke to the PC. When calling remember to flush afterwards.
                os.writeBytes("echo " + key_seq + " | " + script_path + " /dev/hidg0 keyboard\n");
                os.flush();
            }

            os.writeBytes("exit\n");
            os.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
