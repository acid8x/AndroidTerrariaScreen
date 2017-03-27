package com.example.acid8xtreme.socket_io_example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class GetServerActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();
        intent.putExtra("URL", "http://192.168.0.110:2222");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
