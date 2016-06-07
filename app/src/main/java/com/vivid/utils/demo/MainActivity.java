package com.vivid.utils.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.vivid.utils.Md5Util;
import com.vivid.utils.UserIdUtil;

public class MainActivity extends AppCompatActivity {
    TextView md5Text, userIdText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        md5Text = (TextView) findViewById(R.id.apk_md5);
        userIdText = (TextView) findViewById(R.id.user_id);
        GAManager.init(this.getApplicationContext());
        String userId = UserIdUtil.getUniqueUId(this);
        Log.d("CommonUtils", "user id: " + userId);
        userIdText.setText("UserId: " + userId);
        GAManager.reportUserId(userId);
        Md5Util.calculateApkMd5Async(this, new Md5Util.OnGetMd5() {
            @Override
            public void onGetMd5(final String md5) {
                Log.d("CommonUtils", "apk md5: " + md5);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        md5Text.setText("APK MD5:" + md5);
                    }
                });
            }
        });
    }
}
