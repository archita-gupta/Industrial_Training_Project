package com.example.quotescardmaker.image;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quotescardmaker.R;
import com.example.quotescardmaker.activity.ActivityImages;

/**
 * adapter for gridview in ActivityImages
 *
 * @see ActivityImages
 */
public class GridViewAdapter extends ArrayAdapter<ImageItem> {
    final String FOLDER_NAME = "Quotes";
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();
    private AlphaAnimation buttonClick = new AlphaAnimation(5F, 0.1F);

    /**
     * constructor
     *
     * @param context
     * @param layoutResourceId
     * @param data
     */
    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            holder.share = (LinearLayout) row.findViewById(R.id.button2);
            holder.delete = (LinearLayout) row.findViewById(R.id.button1);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ImageItem item = data.get(position);
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageBitmap(item.getImage());
        final File f = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME + File.separator + item.getTitle());
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent picMessageIntent = new Intent(android.content.Intent.ACTION_SEND);
                picMessageIntent.setType("image/jpeg");
                picMessageIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                context.startActivity(Intent.createChooser(picMessageIntent, "Share your Quotes using:"));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage(position, item.getImage(), item.getTitle());
            }
        });
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        LinearLayout share, delete;
        ImageView image;
    }

    /**
     * delete image in SD card
     *
     * @param image
     * @param tittle
     */
    public void deleteImage(final int position, Bitmap image, final String tittle) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("Delete Confirmation");
        // Setting Dialog Message
        alertDialog.setMessage("Are Yyou sure want to delete " + tittle + "?");

        // On pressing Settings button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + FOLDER_NAME + File.separator + tittle);
                f.delete();
                Toast.makeText(context, "Delete succesfull", Toast.LENGTH_SHORT).show();
                data.remove(position);
                notifyDataSetChanged();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}