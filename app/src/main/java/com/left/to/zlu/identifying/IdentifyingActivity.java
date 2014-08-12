package com.left.to.zlu.identifying;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;


public class IdentifyingActivity extends Activity {

    private static String TAG = "IdentifyingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identifying);

        LinearLayout blurLayout = (LinearLayout) findViewById(R.id.blur_background);

        long imageId = 57L;
        Uri imageUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageId);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Some description ABC_XYZ");
        getContentResolver().update(imageUri, contentValues, null, null);
        String desc = getDescriptionFromURI(imageUri);
        Log.d(TAG, "Image description: " + desc);

        String pathName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/.thumbnails/" + "1407794490424.jpg";
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        BitmapDrawable backgroundDrawable = new BitmapDrawable(getResources(), bitmap);
        try {
            ExifInterface exifInterface = new ExifInterface(pathName);
            final String comment = exifInterface.getAttribute("UserComment");
            final String description = exifInterface.getAttribute("ImageDescription");
            exifInterface.setAttribute("ImageDescription", "Something New");
            exifInterface.setAttribute("UserComment", "does comment work?");
            exifInterface.saveAttributes();
            if (description != null) {
                Log.d(TAG, description);
            }
            if (comment != null) {
                Log.d(TAG, comment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            blurLayout.setBackgroundDrawable(backgroundDrawable);
        } else {
            blurLayout.setBackground(backgroundDrawable);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.identifying, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getDescriptionFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
        String description = cursor.getString(idx);
        return description;
    }
}
