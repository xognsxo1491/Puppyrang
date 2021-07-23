package com.portfolio.puppy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;

import com.sun.mail.iap.ByteArray;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class ImageUtil {

    // 이미지 리사이징
    public Bitmap resize(Context context, Uri uri, int resize) {
        Bitmap resizeBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options); // 1번

            int width = options.outWidth;
            int height = options.outHeight;
            int samplesize = 1;

            while (width / 2 >= resize && height / 2 >= resize) {//2번
                width /= 2;
                height /= 2;
                samplesize *= 2;
            }

            options.inSampleSize = samplesize;
            resizeBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return resizeBitmap;
    }

    // uri to bitmap
    public Bitmap getCapturedImage(Uri selectedPhotoUri, Context context) {
        Bitmap bitmap = null;

        try {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap =  MediaStore.Images.Media.getBitmap(context.getContentResolver(), selectedPhotoUri);

            } else {
                ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), selectedPhotoUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    // bitmap to string
    public String bitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
