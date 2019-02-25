package com.example.quotescardmaker.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.widget.Toast;

import com.example.quotescardmaker.R;
import com.example.quotescardmaker.data.AssetFontHandler;
import com.example.quotescardmaker.data.Const;
import com.example.quotescardmaker.data.PermissionUtil;
import com.example.quotescardmaker.data.Tools;

/**
 * his activity is first time launch of app
 */
public class ActivitySplash extends Activity {

    final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            startMainActivity();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }


    private void startMainActivity() {
        // go to the main activity
        Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
        startActivity(i);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        // kill current activity
        ActivitySplash.this.finish();
    }

    @Override
    protected void onResume() {
        if(!PermissionUtil.isAllPermissionGranted(this)){
            showDialogPermission();
        }else{
            File folder1 = new File(Const.FOLDER_NAME_FULL);
            folder1.mkdir();
            File folder3 = new File(Const.BG_IMG_FOLDER);
            folder3.mkdir();
            File folder2 = new File(Const.FONT_FOLDER);
            folder2.mkdir();

            new AssetFontHandler(getApplicationContext()).copyAssets();

            new Timer().schedule(task, 3000);
        }
        super.onResume();
    }

    private void showDialogPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_permission));
        builder.setMessage(getString(R.string.dialog_content_permission));
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                PermissionUtil.goToPermissionSettingScreen(ActivitySplash.this);
            }
        });
        builder.show();
    }

}
