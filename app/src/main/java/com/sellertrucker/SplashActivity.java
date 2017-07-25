package com.sellertrucker;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;

import org.json.JSONException;

public class SplashActivity extends AppCompatActivity {
    Handler handler;
    Intent intent;
    static Client client;
    static NotificationCompat.Builder builder;
    static NotificationManager mNotificationManager;
    static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler = new Handler();
        activity = this;




        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client = new Client("169.56.80.26", 5001);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                intent = new Intent(SplashActivity.this, MainActivity.class);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });

            }
        }).start();
    }




}
