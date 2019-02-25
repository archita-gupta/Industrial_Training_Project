package com.example.quotescardmaker.data;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.quotescardmaker.R;


public class Tools {

    public static float getAPIVerison() {
        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", " API" + e.getMessage());
        }

        return f.floatValue();
    }

    private static boolean isLolipopOrHigher() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public static void systemBarLolipop(Activity act){
        if (getAPIVerison() >= 5.0) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static void showDialogPermission(final Activity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.dialog_title_permission));
        builder.setMessage(activity.getString(R.string.dialog_content_permission));
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                PermissionUtil.goToPermissionSettingScreen(activity);
            }
        });
        builder.show();
    }

}
