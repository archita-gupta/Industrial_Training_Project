package com.example.quotescardmaker.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quotescardmaker.R;
import com.example.quotescardmaker.data.PermissionUtil;
import com.example.quotescardmaker.data.Tools;
import com.example.quotescardmaker.image.GridViewAdapter;
import com.example.quotescardmaker.image.ImageItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


/**
 *  this Activity class when showing image in gallery
 */
public class ActivityImages extends AppCompatActivity {

    private AdView mAdView;

    // folder for image
    final String FOLDER_NAME = "Quotes";
    private GridView gridView;
    private GridViewAdapter customGridAdapter;
    private ArrayList<ImageItem> imageItems;

    private LinearLayout lyt_no_item;

    // toolbar
    private Toolbar toolbar;
    public ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagelib);

        initToolbar();

        // catch extras name from MainActivity
        Bundle bundle = this.getIntent().getExtras();
        String name = bundle.getString("name");


        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


        // pop up image
        if (!name.equals("null")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME + File.separator + name);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
                // call method to preview image
                previewImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        lyt_no_item = (LinearLayout) findViewById(R.id.lyt_no_item);

        // declare n fill gridView with qoutes card
        gridView = (GridView) findViewById(R.id.gridView);
        customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
        gridView.setAdapter(customGridAdapter);

        if (getData().size() == 0) {
            lyt_no_item.setVisibility(View.VISIBLE);
        }else{
            lyt_no_item.setVisibility(View.GONE);
        }


        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String nama = imageItems.get(position).getTitle();
                //File f=new File(Environment.getExternalStorageDirectory()+ File.separator + FOLDER_NAME + File.separator+nama);
                Toast.makeText(ActivityImages.this, nama, Toast.LENGTH_SHORT).show();
                previewImage(imageItems.get(position).getImage());

            }
        });

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Quotes Gallery");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    /**
     * retrieve data from local database
     *
     * @return ArrayList<ImageItem>
     */
    private ArrayList<ImageItem> getData() {
        imageItems = new ArrayList<ImageItem>();

        // retrieve String drawable array
        File file2, file = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME + File.separator);
        File[] list = file.listFiles();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int count = 0;
        for (File f : list) {
            String name = f.getName();
            file2 = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME + File.separator + name);
            if (name.endsWith(".png")) {
                count++;
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(file2), null, options);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                imageItems.add(new ImageItem(bitmap, name));
            }
            //Toast.makeText(MainImageLibrary.this, count+"",Toast.LENGTH_SHORT).show();
        }

        return imageItems;

    }

    /**
     * Preview card when first create
     *
     * @param image
     */
    public void previewImage(Bitmap image) {
        final Dialog dialog = new Dialog(ActivityImages.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.preview_image);
        ImageView img = (ImageView) dialog.findViewById(R.id.imageView1);
        img.setImageBitmap(image);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        if(!PermissionUtil.isAllPermissionGranted(this)) Tools.showDialogPermission(this);
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }

    /**
     * Called before the activity is destroyed
     */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

}
