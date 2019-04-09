package peekingduckapp.peekingduck;

import android.os.Handler;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {
    private String[] script;
    private int default_delay = 50;
    private String script_path;
    private static final HashMap<Character, String> key_map = new HashMap<Character, String>() {{ // UK Keyboard
        put(' ', "space");
        put('\n', "enter");
        put('.', "period");
        put('!', "left-shift 1");
        put('`', "backquote");
        put('~', "left-alt kp-1 kp-2 kp-6");
        put('+', "kp-plus");
        put('=', "equal");
        put('_', "left-shift minus");
        put('-', "minus");
        put('"', "left-shift 2");
        put('\'', "quote");
        put(':', "left-shift semicolon");
        put(';', "semicolon");
        put('<', "left-shift comma");
        put(',', "comma");
        put('>', "left-shift period");
        put('?', "left-shift slash");
        put('\\', "left-alt kp-9 kp-2");
        put('|', "left-alt kp-1 kp-2 kp-4");
        put('/', "slash");
        put('{', "left-shift lbracket");
        put('}', "left-shift rbracket");
        put('(', "left-shift 9");
        put(')', "left-shift 0");
        put('[', "lbracket");
        put(']', "rbracket");
        put('#', "left-alt kp-3 kp-5");
        put('£', "left-shift 3");
        put('$', "left-shift 4");
        put('%', "left-shift 5");
        put('^', "left-shift 6");
        put('&', "left-shift 7");
        put('*', "left-shift 8");
    }};
    private String last_cmd = "";
    private String last_str = "";

    public Interpreter(String[] script, String script_path) {
        this.script_path = script_path;
        this.script = script;
        Log.d("SCRIPT", "Script Path: " + script_path);
    }

    public void run() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Process process = null;
                try {
                    process = Runtime.getRuntime().exec("su");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                for(int i = 0; i < script.length; i++ ) {
                    String line = script[i];
                    int index = line.indexOf(' ');
                    String cmd = index > -1 ? line.substring(0, index) : line;
                    String param = index > -1 ? line.substring(index+1) : "";
//                    Log.d("SCRIPT", "Line: " + line);
//                    Log.d("SCRIPT", "Command: " + cmd);
//                    Log.d("SCRIPT", "Param: " + param);

                    if(cmd.equals("DEFAULT_DELAY") || cmd.equals("DEFAULTDELAY")) {
                        last_cmd = cmd;
                        default_delay = Integer.parseInt(param); // TODO: Validate input
                    } else if(cmd.equals("REM")) {
                        continue;
                    } else if(cmd.equals("DELAY")) {
                        last_cmd = cmd;
                        try {
                            Thread.sleep(Integer.parseInt(param)); // TODO: Validate input
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if(cmd.equals("STRING")) { // Send a string
                        last_str = param;
                        last_cmd = cmd;
                        send_string(last_str, os);
                    } else if(cmd.equals("GUI") || cmd.equals("WINDOWS")) { // Send windows key
                        last_cmd = "left-meta " + param.toLowerCase();
                        send_key(last_cmd, os);
                    } else if(cmd.equals("ENTER")) { // Send enter key
                        last_cmd = "enter";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("APP") || cmd.equals("MENU")) { // Simulates a right click
                        last_cmd = "menu";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("SHIFT")) {
                        if(param.equals("DELETE") || param.equals("END") || param.equals("HOME") || param.equals("INSERT") || param.equals("PAGEUP") || param.equals("PAGEDOWN") || param.equals("SPACE") || param.equals("TAB")){
                            last_cmd = "left-shift " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.indexOf("WINDOWS ") == 0 || param.indexOf("GUI ") == 0) {
                            char ch = Character.toLowerCase(param.charAt(param.indexOf(" ") + 1));
                            last_cmd = "left-shift left-meta " + ch;
                            send_key(last_cmd, os);
                        } else if(param.equals("UPARROW")) {
                            last_cmd = "left-shift up";
                            send_key(last_cmd, os);
                        } else if(param.equals("DOWNARROW")) {
                            last_cmd = "left-shift down";
                            send_key(last_cmd, os);
                        } else if(param.equals("LEFTARROW")) {
                            last_cmd = "left-shift left";
                            send_key(last_cmd, os);
                        } else if(param.equals("RIGHTARROW")) {
                            last_cmd = "left-shift right";
                            send_key(last_cmd, os);
                        }
                    } else if(cmd.equals("ALT")) {
                        if(param.equals("END") || param.equals("SPACE") || param.equals("TAB")) {
                            last_cmd = "left-alt " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("ESC") || param.equals("ESCAPE")) {
                            last_cmd = "left-alt escape";
                            send_key(last_cmd, os);
                        } else if(param.equals("F1") || param.equals("F2") || param.equals("F3") || param.equals("F4") || param.equals("F5") || param.equals("F6") || param.equals("F7") || param.equals("F8") || param.equals("F9") || param.equals("F10") || param.equals("F11") || param.equals("F12")) {
                            last_cmd = "left-alt " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("")){
                            last_cmd = "left-alt";
                            send_key(last_cmd, os);
                        }
                        else {
                            last_cmd = "left-alt " + param.toLowerCase();
                            send_key(last_cmd, os);
                        }
                    } else if(cmd.equals("CONTROL") || cmd.equals("CTRL")) {
                        if(param.equals("BREAK") || param.equals("PAUSE")) {
                            last_cmd = "left-ctrl pause";
                            send_key(last_cmd, os);
                        } else if(param.equals("F1") || param.equals("F2") || param.equals("F3") || param.equals("F4") || param.equals("F5") || param.equals("F6") || param.equals("F7") || param.equals("F8") || param.equals("F9") || param.equals("F10") || param.equals("F11") || param.equals("F12")) {
                            last_cmd = "left-ctrl " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("ESCAPSE") || param.equals("ESC")) {
                            last_cmd = "left-ctrl escape";
                            send_key(last_cmd, os);
                        } else if(param.equals("")) {
                            last_cmd = "left-ctrl";
                            send_key(last_cmd, os);
                        } else {
                            last_cmd = "left-ctrl " + param.toLowerCase();
                            send_key(last_cmd, os);
                        }
                    } else if(cmd.equals("DOWNARROW") || cmd.equals("DOWN")) {
                        last_cmd = "down";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("UPARROW") || cmd.equals("UP")) {
                        last_cmd = "up";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("LEFTARROW") || cmd.equals("LEFT")) {
                        last_cmd = "left";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("RIGHTARROW") || cmd.equals("RIGHT")) {
                        last_cmd = "right";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("BREAK") || cmd.equals("PAUSE")) {
                        last_cmd = "pause";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("ESC") || cmd.equals("ESCAPE")) {
                        last_cmd = "escape";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("PRINTSCREEN")) {
                        last_cmd = "print";
                        send_key(last_cmd, os);
                    } else if (cmd.equals("CAPSLOCK") || cmd.equals("DELETE") || cmd.equals("END") || cmd.equals("HOME") || cmd.equals("INSERT") || cmd.equals("NUMLOCK") || cmd.equals("PAGEUP") || cmd.equals("PAGEDOWN") || cmd.equals("SCROLLLOCK") || cmd.equals("SPACE") || cmd.equals("TAB")) {
                        last_cmd = cmd.toLowerCase();
                        send_key(last_cmd, os);
                    } else if(cmd.equals("F1") || cmd.equals("F2") || cmd.equals("F3") || cmd.equals("F4") || cmd.equals("F5") || cmd.equals("F6") || cmd.equals("F7") || cmd.equals("F8") || cmd.equals("F9") || cmd.equals("F10") || cmd.equals("F11") || cmd.equals("F12")) {
                        last_cmd = cmd.toLowerCase();
                        send_key(last_cmd, os);
                    } else if(cmd.equals("ALT-SHIFT")) {
                        last_cmd = "left-shift left-alt";
                        send_key(last_cmd, os);
                    } else if(cmd.equals("CTRL-ALT")) {
                        if(param.equals("BREAK") || param.equals("PAUSE")) {
                            last_cmd = "left-ctrl left-alt pause";
                            send_key(last_cmd, os);
                        } else if(param.equals("END") || param.equals("SPACE") || param.equals("TAB") || param.equals("DELETE")) {
                            last_cmd = "left-ctrl left-alt " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("F1") || param.equals("F2") || param.equals("F3") || param.equals("F4") || param.equals("F5") || param.equals("F6") || param.equals("F7") || param.equals("F8") || param.equals("F9") || param.equals("F10") || param.equals("F11") || param.equals("F12")) {
                            last_cmd = "left-ctrl left-alt " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("ESC") || param.equals("ESCAPE")) {
                            last_cmd = "left-ctrl left-alt " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("")) {
                            last_cmd = "left-ctrl left-alt";
                            send_key(last_cmd, os);
                        } else {
                            last_cmd = "left-ctrl left-alt " + param.toLowerCase();
                            send_key(last_cmd, os);
                        }
                    } else if(cmd.equals("CTRL-SHIFT")) {
                        if(param.equals("BREAK") || param.equals("PAUSE")) {
                            last_cmd = "left-ctrl left-shift pause";
                            send_key(last_cmd, os);
                        } else if(param.equals("END") || param.equals("SPACE") || param.equals("TAB") || param.equals("DELETE")) {
                            last_cmd = "left-ctrl left-shift " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("F1") || param.equals("F2") || param.equals("F3") || param.equals("F4") || param.equals("F5") || param.equals("F6") || param.equals("F7") || param.equals("F8") || param.equals("F9") || param.equals("F10") || param.equals("F11") || param.equals("F12")) {
                            last_cmd = "left-ctrl left-shift " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("ESC") || param.equals("ESCAPE")) {
                            last_cmd = "left-ctrl left-shift " + param.toLowerCase();
                            send_key(last_cmd, os);
                        } else if(param.equals("")) {
                            last_cmd = "left-ctrl left-shift";
                            send_key(last_cmd, os);
                        } else {
                            last_cmd = "left-ctrl left-shift " + param.toLowerCase();
                            send_key(last_cmd, os);
                        }
                    } else if(cmd.equals("REPEAT")) {
                        if(last_cmd.equals("DELAY") || last_cmd.equals("DEFAULTDELAY") || cmd.equals("DEFAULT_DELAY")) {
                            Log.e("SCRIPT", "Invalid REPEAT: Cannot repeat DELAY or DEFAULTDELAY");
                        } else {
                            int repeat_count = Integer.parseInt(param); //TODO: Validate input
                            for(int j = 0; j < repeat_count; j++) {
                                if(last_cmd.equals("STRING")) {
                                    send_string(last_str, os);
                                } else {
                                    send_key(last_cmd, os);
                                }
                            }
                        }
                    } else if(!cmd.equals("")) {
                        Log.e("SCRIPT", "Unknown command: " + cmd);
                    }

                    if(default_delay > 0) {
                        try {
                            Thread.sleep(default_delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    os.writeBytes("exit\n");
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void send_key(String key, DataOutputStream os) {
                Log.d("SCRIPT", "Sending Key Seq: " + key);
                try{
                    os.writeBytes("echo " + key + " | " + script_path + " /dev/hidg0 keyboard\n");
                    os.flush();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            private void send_string(String str, DataOutputStream os) {
                Log.d("SCRIPT", "Sending String: " + str);
                char[] keys = str.toCharArray();

                for (int i = 0; i < keys.length; i++) {
                    String key_seq;
                    if(key_map.containsKey(keys[i])) {
                        key_seq = key_map.get(keys[i]);
                    }
                    else if((int) keys[i] > 64 && (int) keys[i] < 91){ // Uppercase character (A-Z)
                        key_seq = "left-shift " + ("" + keys[i]).toLowerCase();
                    }
                    else {
                        key_seq = "" + keys[i];
                    }

                    send_key(key_seq, os);
                }
            }
        });
    }
}
