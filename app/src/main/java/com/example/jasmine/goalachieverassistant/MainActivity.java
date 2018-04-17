package com.example.jasmine.goalachieverassistant;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import com.example.jasmine.goalachieverassistant.Fragments.Fragments.InboxMenu;
import com.example.jasmine.goalachieverassistant.Fragments.Fragments.ProjectMenu;
import com.example.jasmine.goalachieverassistant.Models.ListCategory;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jasmine on 20/03/18.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Realm realm;
    android.support.v4.app.Fragment fragment;
    Bundle saved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saved = savedInstanceState;

        Log.d("GOALS", "onCreate: inside MainActivity ");
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setToolbar(toolbar);
//        if(savedInstanceState==null){
//            displaySelectedScreen(R.id.nav_inbox);
//        }

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
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_goal_list, menu);
    return true;
        }



    private void displaySelectedScreen(int itemId) {

        //creating fragment object
         fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_project:
                fragment = new ProjectMenu();
                break;
            case R.id.nav_inbox:
                fragment = new InboxMenu();
                break;
            case R.id.nav_today:
                fragment = new ProjectMenu();
            case R.id.nav_week:
                fragment = new ProjectMenu();
                break;
            case R.id.nav_all:
                fragment = new ProjectMenu();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                break;
        }
            //replacing the fragment
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

    }

    public void setToolbar(Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu("Lists");
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ListCategory> cat = realm.where(ListCategory.class).findAll();
        for(ListCategory category: cat) {

            if ((!(category.getName().trim().equalsIgnoreCase(getResources().getString(R.string.category_Inbox).trim()))) && (!(category.getName().trim().equalsIgnoreCase(getResources().getString(R.string.category_Project).trim()))))
            {

                subMenu.add(category.getName()).setIcon(R.drawable.ic_list_black_24dp);

            }


        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
        }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GOALS", "onDestroy: inside MainActivity ");
        if(null != realm){
            realm.close();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();

        Log.d("GOALS", "onPause: inside MainActivity ");

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null == fragment){
            displaySelectedScreen(R.id.nav_inbox);
        }else if(fragment.getClass().equals(InboxMenu.class)){
            displaySelectedScreen(R.id.nav_inbox);
        }else if(fragment.getClass().equals(ProjectMenu.class)){
            displaySelectedScreen(R.id.nav_project);
        }else if(fragment.getClass().equals(InboxMenu.class)){
            displaySelectedScreen(R.id.nav_all);
        }else if(fragment.getClass().equals(InboxMenu.class)){
            displaySelectedScreen(R.id.nav_today);
        }else if(fragment.getClass().equals(InboxMenu.class)){
            displaySelectedScreen(R.id.nav_week);
        }



        Log.d("GOALS", "fragment in OnResume");
//        if (true== fragment.getClass().equals(InboxMenu.class)) {
//            InboxMenu ref = (InboxMenu) fragment;
//            ref.refreshDataset();
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, fragment);
//            ft.commit();
//
//        }


    }




}

