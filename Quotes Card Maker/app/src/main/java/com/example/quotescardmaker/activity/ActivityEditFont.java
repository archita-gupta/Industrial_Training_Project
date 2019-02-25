package com.example.quotescardmaker.activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.quotescardmaker.R;
import com.example.quotescardmaker.data.Const;
import com.example.quotescardmaker.data.FileSelect;
import com.example.quotescardmaker.data.GlobalVariable;
import com.example.quotescardmaker.data.PermissionUtil;
import com.example.quotescardmaker.data.Tools;

public class ActivityEditFont extends AppCompatActivity {

    private GlobalVariable global;
    private FloatingActionButton button_addnew;
    private ListView list;
    private LinearLayout lyt_no_item;

    // toolbar
    private Toolbar toolbar;
    public ActionBar actionBar;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_font);
        parent_view = findViewById(android.R.id.content);

        global = (GlobalVariable) getApplication();

        initToolbar();

        lyt_no_item = (LinearLayout) findViewById(R.id.lyt_no_item);
        if (global.getListNewFont().size() == 0) {
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            lyt_no_item.setVisibility(View.GONE);
        }
        button_addnew = (FloatingActionButton) findViewById(R.id.button_addnew);
        list = (ListView) findViewById(R.id.listView_new_font);
        refreshListOfFont();

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            }
        });
        list.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!global.getStringPref(global.S_KEY_FONT, "null").equals(global.getListFileNewFont().get(position).getPath())) {
                    dialogDeleteFontConfirm(position);
                } else {
                    final Snackbar snack = Snackbar.make(parent_view, "Cannot delete font is in use", Snackbar.LENGTH_INDEFINITE);
                    snack.show();
                    snack.setAction("OK", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snack.dismiss();
                        }
                    });
                }
                return false;
            }
        });

        button_addnew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectFile();
            }
        });

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Custom Font");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private static final int FILE_SELECT_CODE = 1;

    public void startSelectFile() {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent.createChooser(intent, "Select File"), FILE_SELECT_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (null == data) {
                return;
            }
            Uri selectedFileUri = data.getData();
            File f;
            if (selectedFileUri.toString().startsWith("file:")) {
                f = new File(selectedFileUri.getPath().toString());
            } else {
                f = new File(FileSelect.getPathFile(selectedFileUri, ActivityEditFont.this));
            }
            String name = f.getName();
            //tv_message.setText(selectedPath +"\n"+Environment.getExternalStorageDirectory()+ File.separator);
            if (name.endsWith(".ttf") || name.endsWith(".TTF") || name.endsWith(".otf") || name.endsWith(".OTF")) {
                dialogAddFontConfirm(f);
                //Toast.makeText(getApplicationContext(), "This is font", Toast.LENGTH_SHORT).show();
            } else {
                dialogInfoMessage();
                //Toast.makeText(getApplicationContext(), "This is not font", Toast.LENGTH_SHORT).show();
            }
        }
    }


    protected void dialogInfoMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid file extension");
        builder.setMessage("Only accept font extension .tff or .otf");
        builder.setNeutralButton("OK", null);
        builder.show();
    }

    protected void dialogAddFontConfirm(final File file_font) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new font confirmation");
        builder.setMessage("Are you sure add font " + file_font.getName() + " ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final File file = new File(Const.FONT_FOLDER + File.separator + file_font.getName());
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                        FileSelect.rescanFolder(ActivityEditFont.this, Const.FONT_FOLDER + File.separator);
                        //message.setText("File:"+file.exists()+" | "+file_font.exists()+"\n"+file_font.getAbsolutePath());
                        global.copy(file_font, file);
                        refreshListOfFont();
                        Snackbar.make(parent_view, "Success add new font", Snackbar.LENGTH_SHORT).show();
                    } else {
                        final Snackbar snack = Snackbar.make(parent_view, "Font already exists", Snackbar.LENGTH_INDEFINITE);
                        snack.show();
                        snack.setAction("OK", new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snack.dismiss();
                            }
                        });
                    }
                } catch (IOException e) {
                    file.delete();
                    Snackbar.make(parent_view, "Failed add new font", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.show();
    }

    protected void dialogDeleteFontConfirm(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete confirmation");
        builder.setMessage("Are you sure delete font " + global.getListNewFont().get(position) + " ?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (global.getListFileNewFont().get(position).delete()) {
                    refreshListOfFont();
                    Snackbar.make(parent_view, "Delete success", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(parent_view, "Delete unsuccessful", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("NO", null);
        builder.show();
    }

    public void refreshListOfFont() {
        list.setAdapter(new ArrayAdapter<String>(ActivityEditFont.this, android.R.layout.simple_list_item_1, global.getListNewFont()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setWidth(LayoutParams.MATCH_PARENT);
                textView.setHeight(LayoutParams.MATCH_PARENT);
                return textView;
            }
        });
        if (global.getListNewFont().size() == 0) {
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            lyt_no_item.setVisibility(View.GONE);
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
                PermissionUtil.goToPermissionSettingScreen(ActivityEditFont.this);
            }
        });
        builder.show();
    }
}
