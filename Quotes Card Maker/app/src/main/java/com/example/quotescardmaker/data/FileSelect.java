package com.example.quotescardmaker.data;

import java.io.File;
import java.io.FileFilter;

import android.app.Activity;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class FileSelect {
	
	public static String getPathFile(Uri contentUri, Activity context) {
        String res = null;
        String[] proj = {MediaStore.Audio.Media.DATA};
        try {
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                res = cursor.getString(column_index);
            }
            cursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
	
	public static void rescanFolder(Activity act, String dest) {
        // Scan files only (not folders);
        File[] files = new File(dest).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });

        String[] paths = new String[files.length];
        for (int co=0; co< files.length; co++)
            paths[co] = files[co].getAbsolutePath();

        Log.i("TAG", "Finished scanning " + paths);

        MediaScannerConnection.scanFile(act, paths, null, null);

        // and now recursively scan subfolders
        files = new File(dest).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        for (int co=0; co<files.length; co++)
            rescanFolder(act, files[co].getAbsolutePath());
    }
	
	private void scanFile(Activity act, String path) {

        MediaScannerConnection.scanFile(act,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }
}
