package it.natter;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import it.natter.dao.Dao;
import it.natter.dao.DataBaseHelper;
import it.natter.drawerMenu.NavDrawerItem;
import it.natter.drawerMenu.NavDrawerListAdapter;
import it.natter.fragment.HomeFragment;
import it.natter.fragment.MapNatterFragment;
import it.natter.fragment.PeopleFragment;
import it.natter.fragment.PositionFragment;
import it.natter.fragment.ProfileFragment;
import it.natter.fragment.SettingsFragment;
import it.natter.utility.Code;
import it.natter.utility.CustomDialogCompleteProfile;

/**
 * Created by francesco on 07/03/14.
 */
public class Core extends FragmentActivity{

    //Parameters menu
    private DrawerLayout menuLayout;
    private ListView menuList;
    private ActionBarDrawerToggle menuToggle;
    private CharSequence menuTitle;

    //Slide menu items
    private String[] menuItemTitles;
    private TypedArray menuItemIcons;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private boolean isShowMap = true;
    private boolean doubleBackToExitPressedOnce = false;

    private int type;
    private int sender;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.core);

        Intent intent = getIntent();

        this.type = intent.getIntExtra("type",-1);
        this.sender = intent.getIntExtra("sender",-1);

        this.manageMenu();
        if(type == -1){
            displayView(2);
        }
        else{
            displayView(0);
        }

        DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        if(Dao.getEmailProfile(db).equals("Not stated")||Dao.getPhoneProfile(db).equals("Not stated")){
            runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    CustomDialogCompleteProfile dialog = new CustomDialogCompleteProfile(db);
                    dialog.show(getFragmentManager(), "");
                }
            });
        }

        //db.close();
    }

    private void manageMenu(){
        //Load slide menu items
        menuItemTitles = getResources().getStringArray(R.array.menu_items);
        //Set title
        menuTitle = menuItemTitles[0];
        //Nav drawer icons from resources
        menuItemIcons = getResources().obtainTypedArray(R.array.menu_icons);
        //Grafichs elemets
        menuLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuList = (ListView) findViewById(R.id.list_slidermenu);

        //Adding nav drawer items to array
        navDrawerItems = new ArrayList<NavDrawerItem>();
        //Home
        navDrawerItems.add(new NavDrawerItem(menuItemTitles[0],menuItemIcons.getResourceId(0, -1)));
        //Profile
        navDrawerItems.add(new NavDrawerItem(menuItemTitles[1],menuItemIcons.getResourceId(1, -1)));
        //Peolple
        navDrawerItems.add(new NavDrawerItem(menuItemTitles[2], menuItemIcons.getResourceId(2, -1)));
        //Position
        navDrawerItems.add(new NavDrawerItem(menuItemTitles[3],menuItemIcons.getResourceId(3, -1)));
        //Map
        navDrawerItems.add(new NavDrawerItem(menuItemTitles[4], menuItemIcons.getResourceId(4, -1)));
        //Settings
        navDrawerItems.add(new NavDrawerItem(menuItemTitles[5],menuItemIcons.getResourceId(5, -1)));


        //Recycle the typed array
        menuItemIcons.recycle();

        //Set onClick
        menuList.setOnItemClickListener(new SlideMenuClickListener());

        //Setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
        menuList.setAdapter(adapter);

        //Enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        menuToggle = new ActionBarDrawerToggle(this, menuLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ){
            public void onDrawerClosed(View view){
                setIcon(menuTitle);
                getActionBar().setTitle(menuTitle);
                //Calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView){

                setIcon("Natter");
                getActionBar().setTitle("Natter");

                /*setIcon(menuTitle);
                getActionBar().setTitle(menuTitle);*/
                //Calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        menuLayout.setDrawerListener(menuToggle);
    }

    /**
     * Slide menu item click listener
     * */
    private class SlideMenuClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position,long id){
            //Display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Toggle nav drawer on selecting action bar app icon/title
        if (menuToggle.onOptionsItemSelected(item)){
            return true;
        }
        //Handle action bar actions click
        switch(item.getItemId()){
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        //If nav drawer is opened, hide the action items
        boolean drawerOpen = menuLayout.isDrawerOpen(menuList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position){
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch(position){
            case 0:
                this.isShowMap = true;
                fragment = new HomeFragment();
                if(this.type!=-1){
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",this.type);
                    bundle.putInt("sender",this.sender);
                    fragment.setArguments(bundle);
                }
                break;
            case 1:
                this.isShowMap = true;
                fragment = new ProfileFragment();
                break;
            case 2:
                this.isShowMap = true;
                fragment = new PeopleFragment();
                break;
            case 3:
                this.isShowMap = true;
                fragment = new PositionFragment();
                break;
            case 4:
                if(isShowMap){
                    this.isShowMap = false;
                    fragment = new MapNatterFragment();
                }
                else{
                    menuLayout.closeDrawer(menuList);
                }
                break;
            case 5:
                this.isShowMap = true;
                fragment = new SettingsFragment();
                break;

            default:
                break;
        }

        if(fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

            //Update selected item title and icon, then close the drawer
            menuList.setItemChecked(position, true);
            menuList.setSelection(position);
            setTitle(menuItemTitles[position]);
            setIcon(menuItemTitles[position]);
            menuLayout.closeDrawer(menuList);
        }
        else{
            if(isShowMap)   Log.e("Core", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title){
        menuTitle = title;
        getActionBar().setTitle(menuTitle);
    }

    public void setIcon(CharSequence title){
        menuItemIcons = getResources().obtainTypedArray(R.array.menu_icons);
        if(title.equals("Home"))    getActionBar().setIcon(menuItemIcons.getResourceId(0, -1));
        else if(title.equals("Profile"))    getActionBar().setIcon(menuItemIcons.getResourceId(1, -1));
        else if(title.equals("People"))    getActionBar().setIcon(menuItemIcons.getResourceId(2, -1));
        else if(title.equals("Position"))    getActionBar().setIcon(menuItemIcons.getResourceId(3, -1));
        else if(title.equals("Map"))    getActionBar().setIcon(menuItemIcons.getResourceId(4, -1));
        else if(title.equals("Settings"))    getActionBar().setIcon(menuItemIcons.getResourceId(5, -1));
        else if(title.equals("Natter"))    getActionBar().setIcon(menuItemIcons.getResourceId(6, -1));
    }

    /**
     * When using the ActionBarDrawerToggle, you must voice it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        //Sync the toggle state after onRestoreInstanceState has occurred.
        menuToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        //Pass any configuration change to the drawer toggls
        menuToggle.onConfigurationChanged(newConfig);
    }

    public boolean settingOnClick(MenuItem item){
        Fragment fragment = new SettingsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        int position = 5;

        this.isShowMap = true;

        menuList.setItemChecked(position, true);
        menuList.setSelection(position);
        setTitle(menuItemTitles[position]);
        setIcon(menuItemTitles[position]);
        menuLayout.closeDrawer(menuList);

        return true;
    }

    @Override
    public void onBackPressed(){
        if(this.doubleBackToExitPressedOnce){
            setResult(Code.CLOSE_ALL);
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void finish(){
        DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        Dao.updateDestroy(db, "map_user", true);
        Dao.updateDestroy(db, "map", true);

        db.close();

        super.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        NatterApplication.activityResumed();
    }

    @Override
    public void onPause() {
        super.onPause();
        NatterApplication.activityPaused();
    }

}

