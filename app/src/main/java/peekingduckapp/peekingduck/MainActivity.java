package peekingduckapp.peekingduck;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private FragmentScripts viewScriptsFragment;
    private FragmentQueue queueFragment;
    private FragmentEdit editFragment;
    private FragmentAdd addFragment;
    private boolean useQueueAdapter;
    NavigationView navigationView;
    private static final int FILE_SELECT_CODE = 0;
    private String loaded_payload_path = null;
    private static String HELLO_WORLD_SCRIPT = "GUI r\nDELAY 500\nSTRING notepad\nENTER\nDELAY 500\nSTRING Hello World!";




    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v("PlugInReceiver", "Action: " + action);
            Log.v("PlugInReceiver", "Context: " + context);
            Bundle extras = intent.getExtras();
            Log.v("PLUG", "Extras: " + extras);

            queueFragment.usb_state_change(action.equals(Intent.ACTION_POWER_CONNECTED));
        }
    };

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        filter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        //Fragment initialisation
        viewScriptsFragment = new FragmentScripts();
        queueFragment = new FragmentQueue();
        editFragment = new FragmentEdit();
        addFragment = new FragmentAdd();

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
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                // close the drawer when you select and item.
                drawer.closeDrawers();

                switch (menuItem.getItemId()) {
                    // Add what happens when you click on the individual menu items here.
                    case R.id.nav_scripts:
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);        //clears the backstack when going back to main screen
                        return true;
                    case R.id.nav_queue:
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, queueFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.nav_add_new:
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, addFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.nav_load_new:
                        showFileChooser();
                        return true;
                    case R.id.nav_github:
                        Intent intent = new Intent(getApplicationContext(),WebViewActivity.class);
                        startActivity(intent);
                }

                return true;
            }
        });


        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState == null)     //if view has not been created yet
            {
                fragmentManager.beginTransaction().replace(R.id.fragment_container, viewScriptsFragment).commit(); //adds AddScriptFragment to mainactivity
                navigationView.setCheckedItem(R.id.nav_scripts);
            }
        }

        request_permissions();

        Intent battery = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = battery.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        if (status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL) {
            int plug = battery.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            if (plug == BatteryManager.BATTERY_PLUGGED_USB) {
                queueFragment.usb_state_change(true);
            }
        }
    }

    private void request_permissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "External Storage permissions are required to store posters. Please allow this", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void switch_fragment_to_edit(Script script) {
        editFragment.setScript(script);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, editFragment).addToBackStack(null).commit();

    }

    public boolean isUseQueueAdapter() {
        return useQueueAdapter;     //this function tells RecyclerViewFragment what adapter to use
    }                               //which determines if it will view the script table or the queue table

    @Override
    public void onBackPressed() {       //more navigation drawer stuff to animate the icon
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (queueFragment.isVisible()) {
            useQueueAdapter = false;
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);        //clears the backstack when going back to main screen
            navigationView.setCheckedItem(R.id.nav_scripts);        //allows the back button to swap the adapter if queue fragment is visible
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("FILE", "File Uri: " + uri.toString());
                    // Get the path
                    Log.d("FILE", "File Path: " + uri.getPath());
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                    String path = uri.getPath();
                    if (path != null) {
                        int index = path.indexOf(":");
                        if (index > -1) {
                            path = path.substring(index + 1);
                            Log.d("FILE", "New Path: " + path);
                            loaded_payload_path = path;
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        if (loaded_payload_path != null) {
            addFragment.set_file_path(loaded_payload_path);
            fragmentManager.beginTransaction().replace(R.id.fragment_container, addFragment).addToBackStack(null).commit();
            loaded_payload_path = null;
        }
    }

    public void add_new_script() {
        fragmentManager.beginTransaction().replace(R.id.fragment_container, addFragment).addToBackStack(null).commit();
    }

    public void edit_queue_item(QueueItem item) {
        editFragment.setQueueItem(item);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, editFragment).addToBackStack(null).commit();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }
}
