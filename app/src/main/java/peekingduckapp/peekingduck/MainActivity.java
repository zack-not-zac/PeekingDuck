package peekingduckapp.peekingduck;

import android.Manifest;
import android.content.Intent;
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
    private boolean useQueueAdapter;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        //Fragment initialisation
        viewScriptsFragment = new FragmentScripts();
        queueFragment = new FragmentQueue();
        editFragment = new FragmentEdit();

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

                switch(menuItem.getItemId()){
                    // Add what happens when you click on the individual menu items here.
                    case R.id.nav_scripts:
                        fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);        //clears the backstack when going back to main screen
                        return true;
                    case R.id.nav_queue:
                        fragmentManager.beginTransaction().replace(R.id.fragment_container,queueFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.nav_interpreter:
                        Intent intent = new Intent(MainActivity.this, InterpreterActivity.class);
                        startActivity(intent);
                        return true;

                }

                return true;
            }
        });



        if(findViewById(R.id.fragment_container)!=null)
        {
            if (savedInstanceState == null)     //if view has not been created yet
            {
                fragmentManager.beginTransaction().replace(R.id.fragment_container,viewScriptsFragment).commit(); //adds AddScriptFragment to mainactivity
                navigationView.setCheckedItem(R.id.nav_scripts);
            }
        }

        request_permissions();
    }

    private void request_permissions() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "External Storage permissions are required to store posters. Please allow this", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    public void switch_fragment_to_edit(Script script) {
        editFragment.setScript(script);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, editFragment).addToBackStack(null).commit();

    }

    public boolean isUseQueueAdapter()
    {
        return useQueueAdapter;     //this function tells RecyclerViewFragment what adapter to use
    }                               //which determines if it will view the script table or the queue table

    @Override
    public void onBackPressed() {       //more navigation drawer stuff to animate the icon
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (queueFragment.isVisible())
        {
            useQueueAdapter = false;
            fragmentManager.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);        //clears the backstack when going back to main screen
            navigationView.setCheckedItem(R.id.nav_scripts);        //allows the back button to swap the adapter if queue fragment is visible
        }
        else {
            super.onBackPressed();
        }
    }
}
