package com.deadman.frcscoutingdata;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Globals.header = namelist();

        String permissions[] = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int PERMISSIONS_ALL = 1;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_ALL);
        }
        else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                &&ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {permissions[1], permissions[2], permissions[3]}, PERMISSIONS_ALL);
        }
        else if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {permissions[2], permissions[3]}, PERMISSIONS_ALL);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        switch(menuItem.getItemId()) {
                            case R.id.nav_pit:
                                Intent intent = new Intent(MainActivity.this, PitScouting.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_crowd:
                                Intent intent2 = new Intent(MainActivity.this, CrowdScouting.class);
                                startActivity(intent2);
                                break;
                            case R.id.nav_master:
                                Intent intent3 = new Intent(MainActivity.this, MasterDevice.class);
                                startActivity(intent3);
                                break;
                            case R.id.nav_settings:
                                Intent intent4 = new Intent(MainActivity.this, Settings.class);
                                startActivity(intent4);
                                break;
                            default:
                                return onNavigationItemSelected(menuItem);
                        }

                        return true;
                    }
                });
    }

    public String namelist() {
        List<String> list2 = new ArrayList<>();
        list2.add("Team");
        list2.add("Match");
        list2.add("Top Hatch Success");
        list2.add("Top Hatch Fail");
        list2.add("Top Cargo Success");
        list2.add("Top Cargo Fail");
        list2.add("Middle Hatch Success");
        list2.add("Middle Hatch Fail");
        list2.add("Middle Cargo Success");
        list2.add("Middle Cargo Fail");
        list2.add("Bottom Hatch Success");
        list2.add("Bottom Hatch Fail");
        list2.add("Bottom Cargo Success");
        list2.add("Bottom Cargo Fail");
        list2.add("Ship Hatch Success");
        list2.add("Ship Hatch Fail");
        list2.add("Ship Cargo Success");
        list2.add("Ship Cargo Fail");
        list2.add("Landing Zone");
        list2.add("Moving in Sandstorm");
        list2.add("Data Entered by");
        list2.add("Match Comments");
        return TextUtils.join(", ", list2);
    }

    // Disable the back button as to not force the memory to be cleared for the activity
    @Override
    public void onBackPressed() {}
}
