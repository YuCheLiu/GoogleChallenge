package com.example.myapplication.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.item.GiftActivity;

import java.util.Calendar;
import java.util.Date;

public class LockScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lock_screen);
        String getUserPickTime = getIntent().getStringExtra("userPickTime");
        Date currentTime = Calendar.getInstance().getTime();
        TextView time = findViewById(R.id.text_currenttime);
        if(currentTime.getHours()/10 ==0){
            if(currentTime.getMinutes()/10==0){
                time.setText("Current time 0"+currentTime.getHours()+":0"+currentTime.getMinutes());
            }else {
                time.setText("Current time 0" + currentTime.getHours() + ":" + currentTime.getMinutes());
            }
        }else{
            if(currentTime.getMinutes()/10==0){
                time.setText("Current time "+currentTime.getHours()+":0"+currentTime.getMinutes());
            }else {
                time.setText("Current time " + currentTime.getHours() + ":" + currentTime.getMinutes());
            }
        }

        TextView wakeUpTime = findViewById(R.id.text_wakeupTime);
        wakeUpTime.setText("Exepected wakeup time: " + getUserPickTime);


        Button unlock = findViewById(R.id.btn_unlockscreen);
        unlock.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                long currentTime = System.currentTimeMillis();


                BlankFragment.sleepingStatus = getSleepingStatus(BlankFragment.sleepTime, currentTime);
                GiftActivity.SleepingStatus = BlankFragment.sleepingStatus;
                // TODO: Testing for gift branch, reset to == 0 after testing

                Log.i("sleeping Status", String.valueOf(BlankFragment.sleepingStatus));
                if (BlankFragment.sleepingStatus == 1){
                    Toast.makeText(v.getContext(), "Your sleeping time is too short to get a gift!", Toast.LENGTH_SHORT).show();
                    showSystemUI();
                    return false;
                } else {
                    Toast.makeText(v.getContext(), "Alice brought a gift during your sleeping time!", Toast.LENGTH_SHORT).show();
                    showSystemUI();
                    Intent intent = new Intent(getBaseContext(), GiftActivity.class);
                    startActivity(intent);
                    return true;
                }
            }
        });

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "To exit, please hold the button.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        String word = "Hello";
        String EXTRA_REPLY = "com.example.myapplication.ui.main.REPLY";

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, word);
        setResult(RESULT_OK, replyIntent);
        finish();
    }

    public static int getSleepingStatus(long sleepTime, long wakeUpTime){

        long duration = (wakeUpTime - sleepTime) / 60000;
        Log.i("duration", String.valueOf(duration));
        if (duration < 30 || duration >= 24 * 60) {
            return 0;
        } else if (duration <= 360) {
            return 1;
        } else {
            return 2;
        }


    }
}
