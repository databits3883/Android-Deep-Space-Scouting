package com.deadman.frcscoutingdata;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andanhm.quantitypicker.QuantityPicker;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CrowdScouting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crowdscouting);

        Globals.pos = getIntent().getIntExtra("posid", 0);

        // Set the team number based on the match number
        teams();

        // Misc Single Selectors
        match_counter();
        launch_counter();
        landing_counter();
        sandstorm_counter();

        // Hatch Selectors
        roc_top_suc_hatch_counter();
        roc_top_fail_hatch_counter();
        roc_mid_suc_hatch_counter();
        roc_mid_fail_hatch_counter();
        roc_bot_suc_hatch_counter();
        roc_bot_fail_hatch_counter();
        ship_suc_hatch_counter();
        ship_fail_hatch_counter();

        // Cargo Selectors
        roc_top_fail_cargo_counter();
        roc_top_suc_cargo_counter();
        roc_mid_fail_cargo_counter();
        roc_mid_suc_cargo_counter();
        roc_bot_fail_cargo_counter();
        roc_bot_suc_cargo_counter();
        ship_fail_cargo_counter();
        ship_suc_cargo_counter();

        reset_info();

        // Navbar menu
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        switch(menuItem.getItemId()) {
                            case R.id.welcome:
                                Intent intent5 = new Intent(CrowdScouting.this, MainActivity.class);
                                startActivity(intent5);
                                break;
                            case R.id.nav_pit:
                                Intent intent = new Intent(CrowdScouting.this, PitScouting.class);
                                startActivity(intent);
                                break;
                            case R.id.nav_crowd:
                                //Intent intent2 = new Intent(CrowdScouting.this, CrowdScouting.class);
                                //startActivity(intent2);
                                //break;
                            case R.id.nav_master:
                                Intent intent3 = new Intent(CrowdScouting.this, MasterDevice.class);
                                startActivity(intent3);
                                break;
                            case R.id.nav_settings:
                                Intent intent4 = new Intent(CrowdScouting.this, Settings.class);
                                startActivity(intent4);
                                break;
                            default:
                                return onNavigationItemSelected(menuItem);
                        }

                        return true;
                    }
                });
    }

    public void onPause(){
        super.onPause();
        int pos = Globals.pos;
        TextView team_num = findViewById(R.id.team_num);

        // Insert current values into Shared Preferences so they can be saved if app is closed
        SharedPreferences.Editor editor = getSharedPreferences("CurrentUser", MODE_PRIVATE).edit();
        editor.putString("name", Name());
        editor.putString("match", Match());
        editor.putString("comments", Comments());
        if (pos == 0){
            editor.putString("team", team_num.getText().toString());
        }
        editor.apply();
    }

    public void onResume(){
        super.onResume();

        // Restore values that were saved when the app is opened again
        SharedPreferences prefs = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String matchnum = prefs.getString("match", "1");
        String comments = prefs.getString("comments", null);
        String teams = prefs.getString("team", null);

        TextView name_field = findViewById(R.id.name_field);
        QuantityPicker picker = findViewById(R.id.match_counter);
        TextView comments_field = findViewById(R.id.comment_field);
        int pos = getIntent().getIntExtra("posid", 0);
        TextView team_num = findViewById(R.id.team_num);

//        Toast.makeText(getApplicationContext(), pos, Toast.LENGTH_SHORT).show();

        if (pos == 0){
            team_num.setText(teams);
        }

        name_field.setText(name);
        if (matchnum != null) {
            picker.setQuantitySelected(Integer.parseInt(matchnum));
        }
        comments_field.setText(comments);
        teams();
    }

    public void buttonClick(View view){
        QuantityPicker picker = findViewById(R.id.match_counter);

        new AlertDialog.Builder(this)
                .setMessage(R.string.confirm_export_dialog_message)
                .setTitle(R.string.confirm_export_dialog_title)
                .setPositiveButton(R.string.confirm_export, (dialog, id) -> {
                    // Generates the QR Code
                    Bitmap myBitmap = QRCode.from(Team() + "," + Match() + "," + getallquestions() + "," + Name() + "," + Comments()).withSize(500, 500).bitmap();
                    Drawable d = new BitmapDrawable(getResources(), myBitmap);
                    ImagePopup imagePopup = new ImagePopup(this);
                    imagePopup.setImageOnClickClose(true);
                    imagePopup.setHideCloseIcon(true);
                    imagePopup.initiatePopup(d);
                    imagePopup.viewPopup();

                    // Write to the CSV
                    write_data();

                    // Remove the text in the comment field
                    reset_info();

                    // Increment the match number
                    picker.setQuantitySelected(picker.getQuantity() + 1);

                    // Set the team number based on the match number
                    teams();
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    // CANCEL
                })
                .show();
    }

    public void write_data(){
        String results = Team() + "," + Match() + "," + getallquestions() + "," + Name() + "," + Comments();
        String header = Globals.header;
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "FRC" + File.separator + "crowd_data.csv");
        try {
            FileWriter outputfile = new FileWriter(file, true);

            CSVWriter writer = new CSVWriter(outputfile, ',',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    "\r\n");
            List<String[]> data = new ArrayList<>();
            if (file.length() == 0) {
                data.add(new String[] {header});
            }
            data.add(new String[] {results});
            writer.writeAll(data);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String read_data(){
        QuantityPicker picker = findViewById(R.id.match_counter);
        int pos = Globals.pos;
        int match = picker.getQuantity();
        String[][] dataArr;
        String test = "";
        if (pos == 0){
            Toast.makeText(getApplicationContext(), "Warning: You are in practice mode", Toast.LENGTH_SHORT).show();
        } else {
            try {
                CSVReader csvReader = new CSVReader(new FileReader(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FRC" + File.separator + "teams.csv")));
                List<String[]> list = csvReader.readAll();
                dataArr = new String[list.size()][];
                dataArr = list.toArray(dataArr);
                test = dataArr[match][pos];
            } catch (IOException e) {
                e.printStackTrace();
            }
            return test;
        }
        return "";
    }

    public void teams(){
        TextView team_num = findViewById(R.id.team_num);
        team_num.setText(read_data());
    }

    public void reset_info(){
        TextView comment_field = findViewById(R.id.comment_field);
        comment_field.setText("");

        String[] text_id = new String[]{"roc_top_suc_hatch_counter",
                "roc_top_fail_hatch_counter",
                "roc_top_suc_cargo_counter",
                "roc_top_fail_cargo_counter",
                "roc_mid_suc_hatch_counter",
                "roc_mid_fail_hatch_counter",
                "roc_mid_suc_cargo_counter",
                "roc_mid_fail_cargo_counter",
                "roc_bot_suc_hatch_counter",
                "roc_bot_fail_hatch_counter",
                "roc_bot_suc_cargo_counter",
                "roc_bot_fail_cargo_counter",
                "ship_suc_hatch_counter",
                "ship_fail_hatch_counter",
                "ship_suc_cargo_counter",
                "ship_fail_cargo_counter",
                "landing_counter",
                "sandstorm_counter",
                "launch_counter",
        };

        for (int i = 0; i < 18; i++) {
            int id = getResources().getIdentifier(text_id[i], "id", getPackageName());
            QuantityPicker temp = findViewById(id);
            temp.setQuantitySelected(0);
        }
    }

    public String Team() {
        TextView team = findViewById(R.id.team_num);
        return team.getText().toString();
    }

    public void match_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.match_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 200);
    }

    public void landing_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.landing_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 3);
    }

    public void sandstorm_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.sandstorm_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 1);
    }

    public String Match() {
        QuantityPicker picker;
        picker = findViewById(R.id.match_counter);
        return String.valueOf(picker.getQuantity());
    }

    public String Name() {
        TextView team = findViewById(R.id.name_field);
        return team.getText().toString();
    }

    public String Comments() {
        TextView team = findViewById(R.id.comment_field);
        return team.getText().toString();
    }

    public void roc_top_suc_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_top_suc_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 4);
    }

    public void roc_top_fail_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_top_fail_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void roc_mid_suc_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_mid_suc_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 4);
    }

    public void roc_mid_fail_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_mid_fail_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void roc_bot_suc_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_bot_suc_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 4);
    }

    public void roc_bot_fail_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_bot_fail_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void ship_suc_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.ship_suc_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 8);
    }

    public void ship_fail_hatch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.ship_fail_hatch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void roc_top_suc_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_top_suc_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 4);
    }

    public void roc_top_fail_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_top_fail_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void roc_mid_suc_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_mid_suc_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 4);
    }

    public void roc_mid_fail_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_mid_fail_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void roc_bot_suc_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_bot_suc_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 4);
    }

    public void roc_bot_fail_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.roc_bot_fail_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void ship_suc_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.ship_suc_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 8);
    }

    public void ship_fail_cargo_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.ship_fail_cargo_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 99);
    }

    public void launch_counter(){
        QuantityPicker quantityPicker = findViewById(R.id.launch_counter);
        int id = quantityPicker.getId();
        counter(id, 0, 3);
    }

    public void counter(int id, int min, int max){
        QuantityPicker picker = findViewById(id);
        picker.setMinQuantity(min);
        picker.setMaxQuantity(max);
        picker.setQuantityPicker(true);
        picker.setTextStyle(QuantityPicker.BOLD);
        picker.setQuantityTextColor(R.color.colorPrimaryDark);
        picker.setTextSize(30);
        picker.setQuantityTextColor(R.color.counter_color);
        picker.setTextStyle(QuantityPicker.NORMAL);
        picker.setQuantityButtonColor(R.color.black);
    }

    public String picker(int id){
        QuantityPicker picker = findViewById(id);
        return String.valueOf(picker.getQuantity());
    }

    public String getallquestions (){
        AtomicReference<String> result = new AtomicReference<>("");
        List<Integer> list = new ArrayList<>();
        list.add(R.id.roc_top_suc_hatch_counter);
        list.add(R.id.roc_top_fail_hatch_counter);
        list.add(R.id.roc_top_suc_cargo_counter);
        list.add(R.id.roc_top_fail_cargo_counter);
        list.add(R.id.roc_mid_suc_hatch_counter);
        list.add(R.id.roc_mid_fail_hatch_counter);
        list.add(R.id.roc_mid_suc_cargo_counter);
        list.add(R.id.roc_mid_fail_cargo_counter);
        list.add(R.id.roc_bot_suc_hatch_counter);
        list.add(R.id.roc_bot_fail_hatch_counter);
        list.add(R.id.roc_bot_suc_cargo_counter);
        list.add(R.id.roc_bot_fail_cargo_counter);
        list.add(R.id.ship_suc_hatch_counter);
        list.add(R.id.ship_fail_hatch_counter);
        list.add(R.id.ship_suc_cargo_counter);
        list.add(R.id.ship_fail_cargo_counter);
        list.add(R.id.landing_counter);
        list.add(R.id.sandstorm_counter);
        list.add(R.id.launch_counter);
        list.forEach(
                (name) -> result.set(result + picker(name) + ",")
        );
        return result.get();
    }

    // Disable the back button as to not force the memory to be cleared for the activity
    @Override
    public void onBackPressed() {}
}