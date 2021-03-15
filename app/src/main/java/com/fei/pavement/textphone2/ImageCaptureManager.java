package com.fei.pavement.textphone2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by donglua on 15/6/23.
 * <p/>
 * <p/>
 * http://developer.android.com/training/camera/photobasics.html
 */
public class ImageCaptureManager {

    private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
    public static final int REQUEST_TAKE_PHOTO = 1;

    private String mCurrentPhotoPath;
    private Context mContext;

    public ImageCaptureManager(Context mContext) {
        this.mContext = mContext;
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            if (!storageDir.mkdir()) {
                throw new IOException();
            }
        }
        File image = new File(storageDir, imageFileName + ".jpg");
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public Intent dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = createImageFile();
            compressBmpFileToTargetSize(photoFile,2000);
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            }
        }

        return takePictureIntent;
    }
    /**
     * 压缩图片到目标大小以下
     *
     * @param file
     * @param targetSize
     */
    public void compressBmpFileToTargetSize(File file, long targetSize) {

        if (file.length() > targetSize) {
            // 每次宽高各缩小一半
            int ratio = 2;
            // 获取图片原始宽高
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            int targetWidth = options.outWidth / ratio;
            int targetHeight = options.outHeight / ratio;

            // 压缩图片到对应尺寸
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            Bitmap result = generateScaledBmp(bitmap, targetWidth, targetHeight, baos, quality);

            // 计数保护，防止次数太多太耗时。
            int count = 0;
            while (baos.size() > targetSize && count <= 10) {
                targetWidth /= ratio;
                targetHeight /= ratio;
                count++;

                // 重置，不然会累加
                baos.reset();
                result = generateScaledBmp(result, targetWidth, targetHeight, baos, quality);
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Log.d(TAG, String.format("compressBmpFileToTargetSize end file.length():%d", file.length()));
    }

    /**
     * 图片缩小一半
     *
     * @param srcBmp
     * @param targetWidth
     * @param targetHeight
     * @param baos
     * @param quality
     * @return
     */
    private Bitmap generateScaledBmp(Bitmap srcBmp, int targetWidth, int targetHeight, ByteArrayOutputStream baos, int quality) {
        Bitmap result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, result.getWidth(), result.getHeight());
        canvas.drawBitmap(srcBmp, null, rect, null);
        if (!srcBmp.isRecycled()) {
            srcBmp.recycle();
        }
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return result;
    }


    public void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        mContext.sendBroadcast(mediaScanIntent);
    }


    public String getCurrentPhotoPath() {
        return mCurrentPhotoPath;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && mCurrentPhotoPath != null) {
            savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
            mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
        }
    }

}
