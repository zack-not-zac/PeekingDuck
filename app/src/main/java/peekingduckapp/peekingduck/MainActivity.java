package peekingduckapp.peekingduck;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;
import android.view.MenuItem;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer; //For the navigation drawer
    public static FragmentManager fragmentManager;
    private ViewScriptsFragment viewScriptsFragment;
    private String script_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        // ----------------------------- STUFF FOR NAV DRAWER - ALL CODE SHOULD BE ADDED UNDERNEATH ---------------------------------------
        //replaces default ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Navigation drawer
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation View Listener
        // Link to developer.android page https://developer.android.com/training/implementing-navigation/nav-drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                // close the drawer when you select and item.
                drawer.closeDrawers();

                switch(menuItem.getItemId()){
                    // Add what happens when you click on the individual menu items here.
                    case R.id.nav_scripts:
                    fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);        //clears the backstack when going back to main screen
                    return true;
                }

                return true;
            }
        });

        /**
         * Make a small toast
         * Used mostly for debugging.
         * @param txt
         */

        viewScriptsFragment = new ViewScriptsFragment();

        if(findViewById(R.id.fragment_container)!=null)
        {
            if (savedInstanceState == null)     //if view has not been created yet
            {
                fragmentManager.beginTransaction().replace(R.id.fragment_container,viewScriptsFragment).commit(); //adds AddScriptFragment to mainactivity
                navigationView.setCheckedItem(R.id.nav_scripts);
            }
        }
    }

    @Override
    public void onBackPressed() {       //more navigation drawer stuff to animate the icon
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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

/*    public void alertUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Message").setTitle("Title");
        AlertDialog dialog = builder.create();
        dialog.show();
    }*/
}
