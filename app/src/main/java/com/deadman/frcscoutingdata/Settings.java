package com.deadman.frcscoutingdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

public class Settings extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        switch(menuItem.getItemId()) {
                            case R.id.welcome:
                                Intent intent5 = new Intent(Settings.this, MainActivity.class);
                                startActivity(intent5);
                                break;
                            case R.id.nav_pit:
                                Intent intent = new Intent(Settings.this, PitScouting.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_crowd:
                                Intent intent2 = new Intent(Settings.this, CrowdScouting.class);
                                startActivity(intent2);
                                break;
                            case R.id.nav_master:
                                Intent intent3 = new Intent(Settings.this, MasterDevice.class);
                                startActivity(intent3);
                                break;
                            case R.id.nav_settings:
                                //Intent intent4 = new Intent(Settings.this, Settings.class);
                                //startActivity(intent4);
                                //break;
                            default:
                                return onNavigationItemSelected(menuItem);
                        }

                        return true;
                    }
                });
    }

    public void delete (View view){
        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_dialog_message)
                .setTitle(R.string.confirm_dialog_title)
                .setPositiveButton(R.string.confirm, (dialog, id) -> {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"Download"+File.separator+"stats.csv");
                    boolean deleted = file.delete();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // CANCEL
                })
                .show();
    }

    // Disable the back button as to not force the memory to be cleared for the activity
    @Override
    public void onBackPressed() {}
}
