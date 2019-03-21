package com.deadman.frcscoutingdata;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner1;
    int selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Globals.header = namelist();

        addItemsOnSpinner1();
        addListenerOnSpinnerItemSelection();

        spinner1.setSelection(0);

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
                            case R.id.welcome:
                                //Intent intent5 = new Intent(MainActivity.this, MainActivity.class);
                                //startActivity(intent5);
                                //break;
                            case R.id.nav_pit:
                                Intent intent = new Intent(MainActivity.this, PitScouting.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_crowd:
                                Intent intent2 = new Intent(MainActivity.this, CrowdScouting.class);
                                intent2.putExtra("posid", spinner1.getSelectedItemPosition());
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

        File frc = new File(Environment.getExternalStorageDirectory() + File.separator + "FRC");
        File robots = new File(Environment.getExternalStorageDirectory() + File.separator + "FRC" + File.separator + "Robots");
        File teams = new File(Environment.getExternalStorageDirectory() + File.separator + "FRC" + File.separator + "teams.csv");
        if (!frc.exists()) {
            frc.mkdirs();
        }
        if (!robots.exists()) {
            robots.mkdirs();
        }
        if(!teams.exists()){
            new AlertDialog.Builder(this)
                    .setMessage(R.string.confirm_missing_team_dialog_message)
                    .setTitle(R.string.confirm_missing_team_dialog_title)
                    .setPositiveButton(R.string.missing_team_export, (dialog, id) -> {
                    })
                    .show();
        }
    }

    public void onPause(){
        super.onPause();

        // Insert current values into Shared Preferences so they can be saved if app is closed
        SharedPreferences.Editor editor = getSharedPreferences("CurrentUser", MODE_PRIVATE).edit();
        editor.putInt("Selection", spinner1.getSelectedItemPosition());
        editor.apply();
        selection = spinner1.getSelectedItemPosition();
    }

    public void onResume(){
        super.onResume();

        // Restore values that were saved when the app is opened again
        SharedPreferences prefs = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        selection = prefs.getInt("Selection", 0);
        spinner1.setSelection(selection);
    }

    public void addItemsOnSpinner1() {

        spinner1 = findViewById(R.id.spinner1);
        List<String> list = new ArrayList<>();
        list.add("Practice Mode");
        list.add("Red 1");
        list.add("Red 2");
        list.add("Red 3");
        list.add("Blue 1");
        list.add("Blue 2");
        list.add("Blue 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
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
        list2.add("Launch Position");
        list2.add("Playing Defense");
        list2.add("Total Hatches");
        list2.add("Total Cargo");
        list2.add("All Cargo/Hatches Scored");
        list2.add("Data Entered by");
        list2.add("Match Comments");
        return TextUtils.join(", ", list2);
    }

    // Disable the back button as to not force the memory to be cleared for the activity
    @Override
    public void onBackPressed() {}
}
