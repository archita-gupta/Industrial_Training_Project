package com.example.quotescardmaker.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

public class AssetFontHandler {

	private Context context;

	public AssetFontHandler(Context context) {
		super();
		this.context = context;
	}

	public void copyAssets() {
		AssetManager assetManager = context.getAssets();
		String[] files = null;
		try {
			files = assetManager.list("font");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		// Analyzing all file on assets subfolder
		for (String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			// First: checking if there is already a target folder
			File folder = new File(Const.FONT_FOLDER);
			boolean success = true;
			if (!folder.exists()) {
				success = folder.mkdir();
			}
			if (success) {
				// Moving all the files on external SD
				try {
					in = assetManager.open("font/" + filename);
					out = new FileOutputStream(Const.FONT_FOLDER + "/" + filename);
					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
				} catch (IOException e) {
					Log.e("ERROR", "Failed to copy asset file: " + filename, e);
				} 
			} else {
				// Do something else on failure
			}
		}
	}

	// Method used by copyAssets() on purpose to copy a file.
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
