package com.example.quotescardmaker.data;

import java.io.File;

import android.Manifest;
import android.os.Environment;

public class Const {

	public static String[] ALL_REQUIRED_PERMISSION = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.READ_PHONE_STATE
	};

	public final static int FILE_SELECT_CODE = 0;
	// folder to save image result
	public final static String FOLDER_NAME="Quotes";
	public final static String FOLDER_NAME_FULL=Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME;
	public final static String FONT_FOLDER=Environment.getExternalStorageDirectory()+ File.separator + Const.FOLDER_NAME + File.separator + ".font";
	public final static String BG_IMG_FOLDER=Environment.getExternalStorageDirectory()+ File.separator + Const.FOLDER_NAME + File.separator + ".bg_image";
}
