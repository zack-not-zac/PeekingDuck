package peekingduckapp.peekingduck;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Interpreter {
    private ArrayList<String> script;
    private int default_delay = 0;
    private String script_path;

    public Interpreter(ArrayList<String> script, String script_path) {
        this.script_path = script_path;
        this.script = script;
    }

    public void run() {
        for(int i = 0; i < script.size(); i++ ) {
            if(default_delay > 0) {
                try {
                    Thread.sleep(default_delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String line = script.get(i);
            int index = line.indexOf(' ');
            String cmd = (index > -1 ? line.substring(0, index) : line).trim().toUpperCase();
            String param = (index > -1 ? line.substring(index+1) : "").trim();
            Log.d("SCRIPT", "Line: " + line);
            Log.d("SCRIPT", "Command: " + cmd);
            Log.d("SCRIPT", "Param: " + param);

            if(index == 0) {
                if(cmd.equals("DEFAULT_DELAY") || cmd.equals("DEFAULTDELAY")) {
                    this.default_delay = Integer.parseInt(param); // TODO: Validate input
                }
            }

            if(cmd.equals("REM")) {
                continue;
            } else if(cmd.equals("DELAY")) {
                try {
                    Thread.sleep(Integer.parseInt(param)); // TODO: Validate input
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if(cmd.equals("STRING")) {
                send_string(param);
            } else if(cmd.equals("GUI") || cmd.equals("WINDOWS")) {
                send_key("left-meta " + param);
            }
        }
    }

    private void send_key(String key) {
        try{
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            os.writeBytes("echo " + key + " | " + script_path + " /dev/hidg0 keyboard\n");
            os.flush();

            os.writeBytes("exit\n");
            os.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void send_string(String str) {
        char[] keys = str.toCharArray();

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
