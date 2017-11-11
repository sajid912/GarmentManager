package com.personal.sajidkhan.garmentmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.Callbacks {

    private HomeFragment homeFragment;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeControls();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            loadThisFragment(Constants.HOME_FRAG_ID);

        } else if (id == R.id.nav_settings) {

            loadThisFragment(Constants.SETTINGS_FRAG_ID);

        } else if (id == R.id.nav_help) {

            loadThisFragment(Constants.HELP_FRAG_ID);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.ADD_GARMENT_REQ_CODE:

                if (resultCode == Constants.ADD_GARMENT_RES_CODE) homeFragment.refreshAdapter();
                break;
        }

    }

    private void initializeControls() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddGarmentsScreen(false, null);
                //Util.showNotificationInPanel("Hi there !!", HomeActivity.this);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseHelper databaseHelper = new DatabaseHelper(HomeActivity.this);
        databaseHelper.openDB();
        boolean isDataPresent = databaseHelper.areGarmentTypesPresent();
        databaseHelper.closeDB();

        if (!isDataPresent) addGarmentTypes();

        loadThisFragment(Constants.HOME_FRAG_ID); // Load home fragment by default.
        navigationView.setCheckedItem(R.id.nav_home);

    }

    private void addGarmentTypes() {
        DatabaseHelper databaseHelper = new DatabaseHelper(HomeActivity.this);
        databaseHelper.openDB();
        databaseHelper.addOneGarmentType(Constants.SHIRTS);
        databaseHelper.addOneGarmentType(Constants.TSHIRTS);
        long count = databaseHelper.addOneGarmentType(Constants.JEANS);
        databaseHelper.closeDB();

        if (count == 3)
            Toast.makeText(HomeActivity.this, "Data loaded ", Toast.LENGTH_SHORT).show();
    }

    private void openAddGarmentsScreen(boolean isEdit, String groupId) {
        Intent intent = new Intent(HomeActivity.this, AddGarmetActivity.class);
        intent.putExtra(Constants.EDIT_GARMENT, isEdit);
        intent.putExtra(Constants.EDIT_GARMENT_GROUP_ID, groupId);
        startActivityForResult(intent, Constants.ADD_GARMENT_REQ_CODE);
    }

    private void loadThisFragment(int fragmentId) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (fragmentId) {
            case Constants.HOME_FRAG_ID:

                homeFragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.content_home, homeFragment).commit();
                getSupportActionBar().setTitle(R.string.yourcollections);
                fab.show();
                break;

            case Constants.SETTINGS_FRAG_ID:

                SettingsFragment settingsFragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.content_home, settingsFragment).commit();
                getSupportActionBar().setTitle(R.string.settings);
                if (fab.isShown()) fab.hide();
                break;

            case Constants.HELP_FRAG_ID:

                HelpFragment helpFragment = new HelpFragment();
                fragmentManager.beginTransaction().replace(R.id.content_home, helpFragment).commit();
                getSupportActionBar().setTitle(R.string.help);
                if (fab.isShown()) fab.hide();
                break;
        }
    }

    @Override
    public void openCollectionWithGroupId(String groupId) {

        openAddGarmentsScreen(true, groupId);
    }
}
