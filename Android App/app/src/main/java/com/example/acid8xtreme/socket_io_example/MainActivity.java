package com.example.acid8xtreme.socket_io_example;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public TextView[] slots;
    public ProgressBar hp = null, mp = null;
    public TextView tvHp = null, tvMp = null;
    public int hhp = 0, mmp = 0;
    private MainFragment mainFragment = null;
    private boolean landscape;
    private int activityInfo, width, selected = -1;
    public double len = -1;
    public long timer = 0;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            String message = msg.getData().getString("MESSAGE");
            int state = 0;
            switch (msg.what) {
                case Constants.MESSAGE_COMPLETE_ITEM:
                    if (message != null) {
                        char[] array = message.toCharArray();
                        int id = 0;
                        String stack = "";
                        String base64 = "";
                        for (int i = 0; i < message.length(); i++) {
                            if (array[i] == ',' && state < 2) state++;
                            else if (state == 0) id = (id * 10) + (array[i] - 48);
                            else if (state == 1) stack += array[i];
                            else base64 += array[i];
                        }
                        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        BitmapDrawable ob = new BitmapDrawable(getResources(), decodedByte);
                        slots[id].setBackground(ob);
                        slots[id].setText(stack);
                        if (stack.equals("0")) slots[id].setTextColor(Color.BLACK);
                        else if (slots[id].getCurrentTextColor() != Color.RED) slots[id].setTextColor(Color.RED);
                    }
                    break;
                case Constants.MESSAGE_STACK_ONLY:
                    if (message != null) {
                        char[] array = message.toCharArray();
                        int id = 0;
                        String stack = "";
                        for (int i = 0; i < message.length(); i++) {
                            if (array[i] == ',' && state < 1) state++;
                            else if (state == 0) id = (id * 10) + (array[i] - 48);
                            else stack += array[i];
                            if (stack.equals("0")) slots[id].setTextColor(Color.BLACK);
                            else if (slots[id].getCurrentTextColor() != Color.RED) slots[id].setTextColor(Color.RED);
                        }
                        slots[id].setText(stack);
                    }
                    break;
                case Constants.MESSAGE_PLAYER_INFO:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int hhp = 0, mmp = 0;
                            int[] val = msg.getData().getIntArray("PLAYERINFO");
                            if (val != null) {
                                hhp = (val[0]*100) / val[1];
                                mmp = (val[2]*100) / val[3];

                                tvHp.setText("" + val[0] + " / " + val[1]);
                                tvMp.setText("" + val[2] + " / " + val[3]);
                            }
                            hp.setProgress(hhp);
                            mp.setProgress(mmp);
                        }
                    });
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int orientation = getResources().getConfiguration().orientation;
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (orientation == 2) {
            landscape = true;
            if (rotation < 2) activityInfo = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            if (rotation > 1) activityInfo = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
        } else if (orientation == 1) {
            landscape = false;
            if (rotation < 2) activityInfo = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            if (rotation > 1) activityInfo = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadTextViewArray();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (savedInstanceState == null) {
            mainFragment = MainFragment.newInstance(mHandler);
            getSupportFragmentManager().beginTransaction().add(mainFragment, "worker").commit();
            mainFragment.setRetainInstance(true);
        }
    }

    @Override
    public void onDestroy() {
        finishAndRemoveTask();
        System.exit(0);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (selected == -1) {
            for (int i = 0; i < 50; i++) {
                if (slots[i] == v) {
                    timer = now();
                    selected = i;
                    break;
                }
            }
        } else {
            for (int i = 0; i < 50; i++) {
                if (slots[i] == v) {
                    if (selected != i) mainFragment.attemptSend("move", "" + selected + "," + i);
                    else if (now() - 500 < timer) mainFragment.attemptSend("use", "" + selected);
                    break;
                }
            }
            selected = -1;
        }
    }

    public long now() {
        return System.currentTimeMillis();
    }

    private void loadTextViewArray() {
        slots = new TextView[50];
        slots[0] = (TextView) findViewById(R.id.tv0);
        slots[1] = (TextView) findViewById(R.id.tv1);
        slots[2] = (TextView) findViewById(R.id.tv2);
        slots[3] = (TextView) findViewById(R.id.tv3);
        slots[4] = (TextView) findViewById(R.id.tv4);
        slots[5] = (TextView) findViewById(R.id.tv5);
        slots[6] = (TextView) findViewById(R.id.tv6);
        slots[7] = (TextView) findViewById(R.id.tv7);
        slots[8] = (TextView) findViewById(R.id.tv8);
        slots[9] = (TextView) findViewById(R.id.tv9);
        slots[10] = (TextView) findViewById(R.id.tv10);
        slots[11] = (TextView) findViewById(R.id.tv11);
        slots[12] = (TextView) findViewById(R.id.tv12);
        slots[13] = (TextView) findViewById(R.id.tv13);
        slots[14] = (TextView) findViewById(R.id.tv14);
        slots[15] = (TextView) findViewById(R.id.tv15);
        slots[16] = (TextView) findViewById(R.id.tv16);
        slots[17] = (TextView) findViewById(R.id.tv17);
        slots[18] = (TextView) findViewById(R.id.tv18);
        slots[19] = (TextView) findViewById(R.id.tv19);
        slots[20] = (TextView) findViewById(R.id.tv20);
        slots[21] = (TextView) findViewById(R.id.tv21);
        slots[22] = (TextView) findViewById(R.id.tv22);
        slots[23] = (TextView) findViewById(R.id.tv23);
        slots[24] = (TextView) findViewById(R.id.tv24);
        slots[25] = (TextView) findViewById(R.id.tv25);
        slots[26] = (TextView) findViewById(R.id.tv26);
        slots[27] = (TextView) findViewById(R.id.tv27);
        slots[28] = (TextView) findViewById(R.id.tv28);
        slots[29] = (TextView) findViewById(R.id.tv29);
        slots[30] = (TextView) findViewById(R.id.tv30);
        slots[31] = (TextView) findViewById(R.id.tv31);
        slots[32] = (TextView) findViewById(R.id.tv32);
        slots[33] = (TextView) findViewById(R.id.tv33);
        slots[34] = (TextView) findViewById(R.id.tv34);
        slots[35] = (TextView) findViewById(R.id.tv35);
        slots[36] = (TextView) findViewById(R.id.tv36);
        slots[37] = (TextView) findViewById(R.id.tv37);
        slots[38] = (TextView) findViewById(R.id.tv38);
        slots[39] = (TextView) findViewById(R.id.tv39);
        slots[40] = (TextView) findViewById(R.id.tv40);
        slots[41] = (TextView) findViewById(R.id.tv41);
        slots[42] = (TextView) findViewById(R.id.tv42);
        slots[43] = (TextView) findViewById(R.id.tv43);
        slots[44] = (TextView) findViewById(R.id.tv44);
        slots[45] = (TextView) findViewById(R.id.tv45);
        slots[46] = (TextView) findViewById(R.id.tv46);
        slots[47] = (TextView) findViewById(R.id.tv47);
        slots[48] = (TextView) findViewById(R.id.tv48);
        slots[49] = (TextView) findViewById(R.id.tv49);
        hp = (ProgressBar) findViewById(R.id.hp);
        mp = (ProgressBar) findViewById(R.id.mp);
        tvHp = (TextView) findViewById(R.id.tvHp);
        tvMp = (TextView) findViewById(R.id.tvMp);

        for (int i = 0; i < 50; i++) slots[i].setOnClickListener(this);
    }
}
