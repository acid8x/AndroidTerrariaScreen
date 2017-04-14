package com.example.acid8xtreme.socket_io_example;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;

public class ConnectActivity extends Activity implements View.OnClickListener{

    private TextView tvIp;
    private Button[] buttons = new Button[13];
    private int[] ids = { R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.buttonD, R.id.buttonB, R.id.buttonC };
    private String ipString = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int index = 0;
        for (char c : Formatter.formatIpAddress(wifiInfo.getIpAddress()).toCharArray()) {
            if (c == '.') index++;
            ipString += c;
            if (index == 3) break;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        setResult(Activity.RESULT_CANCELED);
        tvIp = (TextView) findViewById(R.id.tvIp);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        tvIp.setTextSize(size.y/15);
        tvIp.setText(ipString);
        for (int i = 0; i < 13; i++) {
            buttons[i] = (Button) findViewById(ids[i]);
            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != ids[12]) ViewAnimator.animate(v).textColor(Color.WHITE,Color.RED,Color.WHITE).scale(1f,10f,1f).duration(200).start();
        for (int i = 0; i < 11; i++) {
            if (v.getId() == ids[i]) ipString += buttons[i].getText().toString();
        }
        if (v.getId() == ids[11]) {
            char[] tempArray = ipString.toCharArray();
            ipString = "";
            for (int i = 0; i < tempArray.length-1;i++) ipString += tempArray[i];
        }
        if (v.getId() == ids[12]) {
            Intent intent = new Intent();
            intent.putExtra("URL", "http://"+ipString);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else tvIp.setText(ipString);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
