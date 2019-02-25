package com.example.quotescardmaker.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Application;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quotescardmaker.R;

public class GlobalVariable extends Application {

    public static final String S_KEY_FONT = "s_key_font";
    public static final String S_KEY_COLOR = "s_key_color";
    public static final String S_KEY_FONT_COLOR = "s_key_font_color";
    public static final String I_KEY_FONT_COLOR = "i_key_font_color";
    public static final String I_KEY_SIZE_QUOTE = "i_key_size_quote";
    public static final String I_KEY_SIZE_AUTH = "i_key_size_auth";
    public static final String B_KEY_BG_IMAGE = "b_key_bg_image";
    public static final String I_KEY_Q_ALIGN = "i_key_q_align";
    public static final String I_KEY_A_ALIGN = "i_key_a_align";

    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_CENTER = 2;
    public static final int ALIGN_RIGHT = 3;

    /**
     * Universal shared preference for boolean
     */
    public boolean getBooleanPref(String key_val, boolean def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getBoolean(key_val, def_val);
    }

    public void setBooleanPref(String key_val, boolean val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putBoolean(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference for integer
     */
    public int getIntPref(String key_val, int def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getInt(key_val, def_val);
    }

    public void setIntPref(String key_val, int val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putInt(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference for string
     */
    public String getStringPref(String key_val, String def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getString(key_val, def_val);
    }

    public void setStringPref(String key_val, String val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val,
                MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putString(key_val, val);
        prefEditor.commit();
    }

    public String generateCurrentDate(int format_key) {
        Date curDate = new Date();
        String DateToStr = "";
        // default 11-5-2014 11:11:51
        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        switch (format_key) {
            case 1:

                format = new SimpleDateFormat("dd/MM/yyy");
                DateToStr = format.format(curDate);
                break;

            case 2:
                // May 11, 2014 11:37 PM
                DateToStr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                        DateFormat.SHORT).format(curDate);
                break;
            case 3:
                // 11-5-2014 11:11:51
                format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                DateToStr = format.format(curDate);
                break;
        }
        return DateToStr;
    }

    public List<File> getListFont() {
        File folder = new File(Const.FONT_FOLDER);
        File[] list = folder.listFiles();
        List<File> listfile = new ArrayList<File>();
        for (File f : list) {
            String name = f.getName();
            if (name.endsWith(".ttf") || name.endsWith(".TTF") || name.endsWith(".otf") || name.endsWith(".OTF")) {
                listfile.add(new File(Const.FONT_FOLDER + File.separator + name));
            }
        }
        return listfile;
    }

    public String[] getListNameFont() {
        String[] litsname = new String[getListFont().size()];
        for (int i = 0; i < getListFont().size(); i++) {
            if (getListFont().get(i).getName().contains(".")) {
                litsname[i] = getListFont().get(i).getName().split("\\.")[0];
            } else {
                litsname[i] = getListFont().get(i).getName();
            }
        }
        return litsname;
    }

    public List<File> getListFileNewFont() {
        List<File> litsname = new ArrayList<File>();
        String[] default_font = getResources().getStringArray(R.array.default_font);
        for (int i = 0; i < getListFont().size(); i++) {
            boolean found = false;
            for (int j = 0; j < default_font.length; j++) {
                if (getListFont().get(i).getName().equals(default_font[j])) {
                    found = true;
                }
            }
            if (!found) {
                litsname.add(getListFont().get(i));
            }
        }
        return litsname;
    }

    public List<String> getListNewFont() {
        List<String> litsname = new ArrayList<String>();
        String[] default_font = getResources().getStringArray(R.array.default_font);
        for (int i = 0; i < getListFont().size(); i++) {
            boolean found = false;
            for (int j = 0; j < default_font.length; j++) {
                if (getListFont().get(i).getName().equals(default_font[j])) {
                    found = true;
                }
            }
            if (!found) {
                if (getListFont().get(i).getName().contains(".")) {
                    litsname.add(getListFont().get(i).getName().split("\\.")[0]);
                } else {
                    litsname.add(getListFont().get(i).getName());
                }
            }

        }
        return litsname;
    }

    public void setFontTv(TextView tv, String font_path) {
        Typeface tf = Typeface.createFromFile(font_path);
        //Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/mekanik.ttf");
        tv.setTypeface(tf);
    }

    public void setSavedFont(TextView tv) {
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Open Sans.ttf");
        if (!getStringPref(S_KEY_FONT, "null").equals("null")) {
            tf = Typeface.createFromFile(getStringPref(S_KEY_FONT, "null"));
        }
        tv.setTypeface(tf);
    }

    public void setSavedColor(LinearLayout lyt) {
        if (getBooleanPref(B_KEY_BG_IMAGE, false)) {
            File folder_img = new File(Const.BG_IMG_FOLDER);
            if (folder_img.listFiles().length >= 1) {
                Bitmap bmp = BitmapFactory.decodeFile(folder_img.listFiles()[0].getAbsolutePath());
                Drawable drawable = new BitmapDrawable(bmp);
                lyt.setBackgroundDrawable(drawable);
            }
        } else {
            lyt.setBackgroundColor(Color.parseColor("#1FAEFF"));
            if (!getStringPref(S_KEY_COLOR, "null").equals("null")) {
                lyt.setBackgroundColor(Color.parseColor(getStringPref(S_KEY_COLOR, "null")));
            }
        }
    }

    public boolean isImageMode() {
        return getBooleanPref(B_KEY_BG_IMAGE, false);
    }

    public void deleteImageFile() {
        File folder_img = new File(Const.BG_IMG_FOLDER);
        if (folder_img.exists()) {
            for (int i = 0; i < folder_img.listFiles().length; i++) {
                folder_img.listFiles()[i].delete();
            }
        }
    }
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public int getQuoAlign(){
        return getIntPref(I_KEY_Q_ALIGN, ALIGN_LEFT);
    }
    public void setQuoAlign(int align){
        setIntPref(I_KEY_Q_ALIGN, align);
    }

    public int getAuthAlign(){
        return getIntPref(I_KEY_A_ALIGN, ALIGN_LEFT);
    }
    public void setAuthAlign(int align){
        setIntPref(I_KEY_A_ALIGN, align);
    }

    public void setTexViewAlign(TextView textView, int align){
        switch (align){
            case ALIGN_LEFT :
                textView.setGravity(Gravity.LEFT);
            break;
            case ALIGN_CENTER:
                textView.setGravity(Gravity.CENTER);
            break;
            case ALIGN_RIGHT:
                textView.setGravity(Gravity.RIGHT);
            break;
        }
    }
}
