package com.example.quotescardmaker.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quotescardmaker.activity.ActivityImages;
import com.example.quotescardmaker.data.Const;

/**
 * generator layout to image
 *
 */
public class SaveLayoutAsImage {

	private String currentDateandTime="name";
	private Activity act;
	public SaveLayoutAsImage(Activity act) {
		super();
		this.act=act;
	}
	
	/**
	 * method to generate image form layouts
	 * @param lyt
	 * @param name
	 */
	public void generateImage(LinearLayout lyt, String name){
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		currentDateandTime = sdf.format(new Date());
		File file=new File(Environment.getExternalStorageDirectory()+ File.separator + Const.FOLDER_NAME + File.separator );
        File[] list = file.listFiles();
		String nameOfImage = name+"_"+list.length+".png";
		String descOfImage = "desc"+currentDateandTime;
		File f = new File(Environment.getExternalStorageDirectory()+ File.separator + Const.FOLDER_NAME + File.separator + nameOfImage);
		Bitmap b = Bitmap.createBitmap(lyt.getDrawingCache());
		//lyt_card.setDrawingCacheEnabled(false);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		try {
			if(f.exists()){
				Toast.makeText(act, "Delete", Toast.LENGTH_SHORT).show();
				f.delete();
			}
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			Toast.makeText(act,"File saved ",Toast.LENGTH_LONG).show();
			fo.close();
			MediaStore.Images.Media.insertImage(act.getContentResolver(),f.getAbsolutePath(), f.getName(), descOfImage);
			scanFile(f.getAbsolutePath());
			//openInGallery(f.getAbsolutePath());
			lyt.setDrawingCacheEnabled(false);
			lyt.setDrawingCacheEnabled(true);
			
			Bundle bundle = new Bundle();
            bundle.putString("name",nameOfImage);
			Intent i = new Intent(act, ActivityImages.class);
			i.putExtras(bundle);
			act.startActivity(i);

		} catch (Exception e) {
			Log.e("Weats", "Eror save file" + e.toString());
		}
	}
	
	/**
	 * to preview in gallery
	 * @param path
	 */
	private void scanFile(String path) {
		MediaScannerConnection.scanFile(act,
				new String[] { path }, null,
				new MediaScannerConnection.OnScanCompletedListener() {
					public void onScanCompleted(String path, Uri uri) {
						Log.i("TAG", "Finished scanning " + path);
					}
				});
	}
}
