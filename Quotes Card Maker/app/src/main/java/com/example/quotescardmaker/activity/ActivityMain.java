package com.example.quotescardmaker.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;

import com.example.quotescardmaker.R;
import com.example.quotescardmaker.data.Const;
import com.example.quotescardmaker.data.GlobalVariable;
import com.example.quotescardmaker.data.PermissionUtil;
import com.example.quotescardmaker.data.Tools;
import com.example.quotescardmaker.image.SaveLayoutAsImage;
import com.example.quotescardmaker.view.CustomScrollView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * activity work screen
 *
 */
public class ActivityMain extends AppCompatActivity {

    private GlobalVariable global;
    private String[] stringArray;
    private String[] colorCode;

    // UI componen
    private Button bt_save, bt_bg, bt_font,bt_libs,bt_share;
    private TextView tv_quote, tv_author;
    private EditText et_quote, et_author;
    private LinearLayout lyt_card, lyt_font;
    private View parent_view;
    private CustomScrollView scroll_view;
    private ViewGroup _root;

    //for ads
    private InterstitialAd mInterstitialAd;

    // toolbar
    private Toolbar toolbar;
    public ActionBar actionBar;

    // for drag
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(android.R.id.content);

        initToolbar();

        global = (GlobalVariable) getApplication();

        // declare UI componen
        initComponen();

        // handling button on click
        buttonHandel();

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    /**
     * method to handle when button clicked
     */
    private void buttonHandel() {
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
                if (!et_quote.getText().toString().equals("") && !et_author.getText().toString().equals("")) {

                    // call SaveLayoutAsImage class
                    SaveLayoutAsImage saveImage = new SaveLayoutAsImage(ActivityMain.this);
                    saveImage.generateImage(lyt_card, tv_author.getText().toString());

                } else {
                    Toast.makeText(ActivityMain.this, "You must fill Textfield", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.bt_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ActivityMain.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        });

        bt_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
                dialogFont();
            }
        });

        lyt_font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
                dialogFont();
            }
        });

        lyt_font.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(35);
                showInterstitial();
                dialogFont();
                return true;
            }
        });

        bt_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
                dialogBackgroundChooser();
            }
        });


        bt_libs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", "null");
                Intent i = new Intent(ActivityMain.this, ActivityImages.class);
                i.putExtras(bundle);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", "null");
                Intent i = new Intent(ActivityMain.this, ActivityImages.class);
                i.putExtras(bundle);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        // drag handle
        lyt_font.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _xDelta = X - lParams.leftMargin;
                        _yDelta = Y - lParams.topMargin;
                        scroll_view.setEnableScrolling(false);
                        break;
                    case MotionEvent.ACTION_UP:
                        scroll_view.setEnableScrolling(true);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Toast.makeText(getApplicationContext(), "Action Move", Toast.LENGTH_SHORT).show();
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = X - _xDelta;
                        layoutParams.topMargin = Y - _yDelta;
                        layoutParams.rightMargin = -250;
                        layoutParams.bottomMargin = -250;
                        view.setLayoutParams(layoutParams);
                        break;
                }
                _root.invalidate();
                return true;
            }
        });

    }

    /**
     * decalration UI componen
     */
    private void initComponen() {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + Const.FOLDER_NAME);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }

        stringArray = getResources().getStringArray(R.array.arr_main_color_name);
        colorCode = getResources().getStringArray(R.array.arr_main_color_code);

        lyt_card = (LinearLayout) findViewById(R.id.lyt_card);
        lyt_font = (LinearLayout) findViewById(R.id.lyt_font);

        bt_bg = (Button) findViewById(R.id.bt_bg);
        bt_save = (Button) findViewById(R.id.bt_save);
        bt_font = (Button) findViewById(R.id.bt_font);
        bt_libs= (Button)findViewById(R.id.bt_libs);
        bt_share = (Button)findViewById(R.id.bt_share);

        tv_quote = (TextView) findViewById(R.id.tv_quote);
        tv_author = (TextView) findViewById(R.id.tv_author);

        et_quote = (EditText) findViewById(R.id.et_quotes);
        et_author = (EditText) findViewById(R.id.et_author);
        scroll_view = (CustomScrollView) findViewById(R.id.scroll_view);

        String colorcode = global.getStringPref(global.S_KEY_FONT_COLOR, "#FFFFFF");
        tv_quote.setTextColor(Color.parseColor(colorcode));
        tv_author.setTextColor(Color.parseColor(colorcode));

        _root = (ViewGroup) findViewById(R.id.root);

        lyt_card.setDrawingCacheEnabled(true);
        lyt_card.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        lyt_card.layout(0, 0, lyt_card.getMeasuredWidth(), lyt_card.getMeasuredHeight());
        lyt_card.buildDrawingCache(true);
        et_quote.addTextChangedListener(fieldValidatorTextWatcher);
        et_quote.setSelected(false);
        //et_author.setSelected(false);
        et_author.addTextChangedListener(fieldValidatorTextWatcher);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));

        // change font
        global.setSavedFont(tv_quote);
        global.setSavedFont(tv_author);
        tv_quote.setTextSize(global.getIntPref(global.I_KEY_SIZE_QUOTE, 15));
        tv_author.setTextSize(global.getIntPref(global.I_KEY_SIZE_AUTH, 10));
        global.setTexViewAlign(tv_quote, global.getQuoAlign());
        global.setTexViewAlign(tv_author, global.getAuthAlign());
        global.setSavedColor(lyt_card);

        //prepare ads
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }


    /**
     * fieldValidatorTextWatcher is handle when user typing
     */
    TextWatcher fieldValidatorTextWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (et_quote.getText().toString().equals("")) {
                tv_quote.setText("Quote text");
            } else {
                tv_quote.setText(et_quote.getText().toString());
            }
            if (et_author.getText().toString().equals("")) {
                tv_author.setText("author");
            } else {
                tv_author.setText(et_author.getText().toString());
            }

        }
    };

    private Dialog dialog_bg;
    private static final int IMAGE_SELECT_CODE = 1;
    private File image_file = null;

    private void dialogBackgroundChooser() {
        dialog_bg = new Dialog(ActivityMain.this);
        dialog_bg.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog_bg.setContentView(R.layout.dialog_bg_chooser);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog_bg.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LinearLayout preview = (LinearLayout) dialog_bg.findViewById(R.id.preview);
        ListView modeList = (ListView) dialog_bg.findViewById(R.id.listView);
        Button btn_image = (Button) dialog_bg.findViewById(R.id.btn_image);
        TabHost tabs = (TabHost) dialog_bg.findViewById(R.id.tabHost);
        tabs.setup();
        tabs.addTab(tabs.newTabSpec("color").setIndicator("COLOR").setContent(R.id.linearLayout));
        tabs.addTab(tabs.newTabSpec("image").setIndicator("IMAGE").setContent(R.id.linearLayout2));
        tabs.setCurrentTab(0);

        modeList.setAdapter(new ArrayAdapter<String>(ActivityMain.this, android.R.layout.simple_list_item_1, stringArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setBackgroundColor(Color.parseColor(colorCode[position]));
                textView.setTextColor(getResources().getColor(R.color.soft_grey));
                return textView;
            }
        });
        modeList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //initComponen();
                global.setStringPref(global.S_KEY_COLOR, colorCode[position]);
                global.setBooleanPref(global.B_KEY_BG_IMAGE, false);
                global.setSavedColor(lyt_card);
                global.deleteImageFile();
                dialog_bg.dismiss();
            }
        });
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                    imagePickerIntent.setType("image/*");
                    startActivityForResult(imagePickerIntent, IMAGE_SELECT_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        if (global.isImageMode()) {
            ((TextView) dialog_bg.findViewById(R.id.message)).setText("Use image with ratio 2:3 for best preview");
            global.setSavedColor(preview);
        } else {
            preview.setVisibility(View.GONE);
        }

        dialog_bg.findViewById(R.id.button_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_bg.dismiss();
            }
        });
        dialog_bg.findViewById(R.id.button_yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (image_file == null) {
                        Toast.makeText(getApplicationContext(), "Ops!, File not found", Toast.LENGTH_LONG).show();
                        return;
                    }
                    global.deleteImageFile();
                    final File file = new File(Const.BG_IMG_FOLDER + File.separator + image_file.getName());
                    file.createNewFile();
                    global.copy(image_file, file);
                    global.setBooleanPref(global.B_KEY_BG_IMAGE, true);
                    Toast.makeText(getApplicationContext(), "Image background selected", Toast.LENGTH_SHORT).show();
                    global.setSavedColor(lyt_card);
                    dialog_bg.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ops!, IO Error", Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog_bg.show();
        dialog_bg.getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == IMAGE_SELECT_CODE && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = imageReturnedIntent.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                image_file = new File(global.getRealPathFromUri(imageUri));
                String name = image_file.getName();
                Log.d("URI : ", name);
                Drawable drawable = new BitmapDrawable(selectedImage);
                if (dialog_bg.isShowing()) {
                    LinearLayout preview = (LinearLayout) dialog_bg.findViewById(R.id.preview);
                    preview.setVisibility(View.VISIBLE);
                    preview.setBackgroundDrawable(drawable);
                    ((LinearLayout) dialog_bg.findViewById(R.id.image_action)).setVisibility(View.VISIBLE);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void dialogFont() {
        final Dialog dialog = new Dialog(ActivityMain.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_font_style);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TabHost tabs = (TabHost) dialog.findViewById(R.id.tabHost);
        tabs.setup();
        tabs.addTab(tabs.newTabSpec("style").setIndicator("STYLE").setContent(R.id.linearLayout));
        tabs.addTab(tabs.newTabSpec("size").setIndicator("SIZE").setContent(R.id.linearLayout2));
        tabs.addTab(tabs.newTabSpec("color").setIndicator("COLOR").setContent(R.id.linearLayout3));
        tabs.addTab(tabs.newTabSpec("align").setIndicator("ALIGN").setContent(R.id.linearLayout4));
        tabs.setCurrentTab(0);

        final TextView t_quote = (TextView) dialog.findViewById(R.id.tv_quote);
        final TextView t_author = (TextView) dialog.findViewById(R.id.tv_author);
        final SeekBar seek_quote = (SeekBar) dialog.findViewById(R.id.seek_quote);
        final SeekBar seek_author = (SeekBar) dialog.findViewById(R.id.seek_author);
        final ListView list_font = (ListView) dialog.findViewById(R.id.list_font);
        final ListView list_f_color = (ListView) dialog.findViewById(R.id.listView_color);

        // get Last condition
        global.setSavedFont(t_quote);
        global.setSavedFont(t_author);
        t_quote.setTextSize(global.getIntPref(global.I_KEY_SIZE_QUOTE, 15));
        t_author.setTextSize(global.getIntPref(global.I_KEY_SIZE_AUTH, 10));
        String colorcode = global.getStringPref(global.S_KEY_FONT_COLOR, "#FFFFFF");
        t_quote.setTextColor(Color.parseColor(colorcode));
        t_author.setTextColor(Color.parseColor(colorcode));
        global.setTexViewAlign(t_quote, global.getQuoAlign());
        global.setTexViewAlign(t_author, global.getAuthAlign());

        global.setSavedFont(t_quote);
        global.setSavedFont(t_author);
        t_quote.setTextSize(global.getIntPref(global.I_KEY_SIZE_QUOTE, 15));
        t_author.setTextSize(global.getIntPref(global.I_KEY_SIZE_AUTH, 10));
        seek_quote.setProgress(global.getIntPref(global.I_KEY_SIZE_QUOTE, 15) - 15);
        seek_author.setProgress(global.getIntPref(global.I_KEY_SIZE_AUTH, 10) - 10);

        // for font typeface
        list_font.setAdapter(new ArrayAdapter<String>(ActivityMain.this, android.R.layout.simple_list_item_1, global.getListNameFont()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                global.setFontTv(textView, global.getListFont().get(position).getAbsolutePath());
                return textView;
            }
        });
        list_font.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                global.setStringPref(global.S_KEY_FONT, global.getListFont().get(position).getAbsolutePath());
                global.setSavedFont(t_quote);
                global.setSavedFont(t_author);
            }
        });

        // for font color
        list_f_color.setAdapter(new ArrayAdapter<String>(ActivityMain.this, android.R.layout.simple_list_item_1, stringArray) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setBackgroundColor(Color.parseColor(colorCode[position]));
                textView.setTextColor(getResources().getColor(R.color.soft_grey));
                return textView;
            }
        });
        list_f_color.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                global.setStringPref(global.S_KEY_FONT_COLOR, colorCode[position]);
                global.setIntPref(global.I_KEY_FONT_COLOR, position);
                String colorcode = global.getStringPref(global.S_KEY_FONT_COLOR, "#FFFFFF");
                t_quote.setTextColor(Color.parseColor(colorcode));
                t_author.setTextColor(Color.parseColor(colorcode));
            }
        });

        // for text size
        seek_quote.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged1 = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged1 = progress + 15;
                t_quote.setTextSize(progressChanged1);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                global.setIntPref(global.I_KEY_SIZE_QUOTE, seekBar.getProgress() + 15);
            }
        });
        seek_author.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged2 = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged2 = progress + 10;
                t_author.setTextSize(progressChanged2);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                global.setIntPref(global.I_KEY_SIZE_AUTH, seekBar.getProgress() + 10);
            }
        });


        // for align text
        // quotes
        dialog.findViewById(R.id.bt_q_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setQuoAlign(global.ALIGN_LEFT);
                global.setTexViewAlign(t_quote, global.ALIGN_LEFT);
            }
        });
        dialog.findViewById(R.id.bt_q_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setQuoAlign(global.ALIGN_CENTER);
                global.setTexViewAlign(t_quote, global.ALIGN_CENTER);
            }
        });
        dialog.findViewById(R.id.bt_q_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setQuoAlign(global.ALIGN_RIGHT);
                global.setTexViewAlign(t_quote, global.ALIGN_RIGHT);
            }
        });
        // author
        dialog.findViewById(R.id.bt_a_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setAuthAlign(global.ALIGN_LEFT);
                global.setTexViewAlign(t_author, global.ALIGN_LEFT);
            }
        });
        dialog.findViewById(R.id.bt_a_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setAuthAlign(global.ALIGN_CENTER);
                global.setTexViewAlign(t_author, global.ALIGN_CENTER);
            }
        });
        dialog.findViewById(R.id.bt_a_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                global.setAuthAlign(global.ALIGN_RIGHT);
                global.setTexViewAlign(t_author, global.ALIGN_RIGHT);
            }
        });

        // ok handle
        dialog.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                initComponen();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_gallery) {
            showInterstitial();
            Bundle bundle = new Bundle();
            bundle.putString("name", "null");
            Intent i = new Intent(ActivityMain.this, ActivityImages.class);
            i.putExtras(bundle);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (item.getItemId() == R.id.action_add_font) {
            Intent i = new Intent(getApplicationContext(), ActivityEditFont.class);
            startActivity(i);

        } else if (item.getItemId() == R.id.action_rate) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.app.megriweather&hl=en" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }

        } else if (item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setMessage(getString(R.string.about_text));
            builder.setNeutralButton("OK", null);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * show ads
     */
    private void showInterstitial() {
        // Show the ad if it's ready
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onResume() {
        if(!PermissionUtil.isAllPermissionGranted(this)) Tools.showDialogPermission(this);
        super.onResume();
    }

    private void showDialogPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_permission));
        builder.setMessage(getString(R.string.dialog_content_permission));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                PermissionUtil.goToPermissionSettingScreen(ActivityMain.this);
            }
        });
        builder.show();
    }
}
